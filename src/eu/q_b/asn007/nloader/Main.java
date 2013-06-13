package eu.q_b.asn007.nloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import eu.q_b.asn007.nloader.controllers.ActionController;
import eu.q_b.asn007.nloader.helpers.NLoaderConfiguration;
import eu.q_b.asn007.nloader.minecraft.Launcher;
import eu.q_b.asn007.nloader.multiclient.GameServer;
import eu.q_b.asn007.nloader.multiclient.LoadingServer;
import eu.q_b.asn007.nloader.theming.Theme;
import eu.q_b.asn007.nloader.theming.ThemeLoader;
import eu.q_b.asn007.nloader.threading.InitBackgroundWorkerThread;

public class Main extends Application {

	
    public static final String MAIN_SCREEN = "MainScene";
    public static final String MAIN_SCREEN_FXML = "/MainScene.fxml";
    
    public static final String SETTINGS_SCREEN = "Settings";
    public static final String SETTINGS_SCREEN_FXML = "/Settings.fxml";
    
    public static final ResourceBundle loc = ResourceBundle.getBundle("nloader");
	public static Main _instance;
	public static String session;
	public static String login;
	public Stage primaryStage;
	
	public boolean forceUpdate = false;
	public boolean launcherBusy = false;
	
	public ArrayList<GameServer> servers;
	public GameServer currentServer;
	public LoadingServer ls;
	
	public NLoaderConfiguration config;
	
	public MediaPlayer player;
	public boolean playing = false;
	private boolean innerTheme = true;
	
	public static Theme theme;
	
	@Override
	public void start(Stage primaryStage) {
		_instance = this;
		config = new NLoaderConfiguration(new File(BaseProcedures.getWorkingDirectory() + File.separator + LauncherConf.nloaderConfiguration));
		BaseProcedures.log("*** nLoader ***", Main.class);
		if(LauncherConf.minecraftLaunchType != LaunchType.COMMAND) {
			BaseProcedures.log("Checking for memory conditions, available " + BaseProcedures.toMegabytes(Runtime.getRuntime().totalMemory()) + "MB of memory...", getClass());
			int mem = config.getInteger("memory", 1024);
			long curMem = BaseProcedures.toMegabytes(Runtime.getRuntime().totalMemory());
			if(mem - curMem > 200 || mem - curMem < 0) {
				BaseProcedures.log("Relaunching with correct memory...", getClass());
				ArrayList<String> params = new ArrayList<String>();
				params.add(BaseProcedures.getJavaExecutable());
				params.add("-jar");
				params.add("-Xms" + mem + "M");
				params.add(BaseProcedures.getMyJarName());
				ProcessBuilder pb = new ProcessBuilder(params);
				try {
					if(!LauncherConf.devEnv)
						pb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					BaseProcedures.log("Unable to relaunch!", getClass());
				}
				if(!LauncherConf.devEnv)
					System.exit(0);
			}
		}
        BaseProcedures.log("Initializing...", Main.class);
		System.setProperty("minecraft.applet.WrapperClass",
				Launcher.class.getCanonicalName()); // Fuck Forge
		if(!config.getString("version").equals(LauncherConf.launcherVersion)) {
			BaseProcedures.log("Found configuration file for an old launcher! Wiping configuration and reloading...", getClass());
			config.writeDefault();
		}
		try {
			initThemingSystem();
		} catch(Exception e) {
			e.printStackTrace();
			BaseProcedures.log("Failed to load online theme, using local one!", getClass());
			innerTheme = true;
		}
		try {
			//URL uri = Main.class.getResource("/MainScene.fxml");
			BaseProcedures.log("Loading scene FXML...", Main.class);
			Parent p   = FXMLLoader.load(ThemeLoader.getFXMLFromTheme(theme, "MainScene"), loc);
			BaseProcedures.log("Adding stylesheets...", Main.class);
			p.getStylesheets().add(ThemeLoader.getStyleSheetFromTheme(theme));
			BaseProcedures.log("Setting up stage...", Main.class);
			primaryStage.setScene(new Scene(p));
			if(innerTheme) {
				primaryStage.setWidth(LauncherConf.SCENE_WIDTH + LauncherConf.BORDERS_WIDTH);
				primaryStage.setHeight(LauncherConf.SCENE_HEIGHT + LauncherConf.WINDOWS_BAR_HEIGHT);
			} else {
				primaryStage.setHeight(LauncherConf.WINDOWS_BAR_HEIGHT + theme.SCENE_HEIGHT);
				primaryStage.setWidth(LauncherConf.BORDERS_WIDTH + theme.SCENE_WIDTH);
			}
			primaryStage.setResizable(false);
			primaryStage.setTitle(loc.getString("nloader.window.main.title"));
			if(theme == null) primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/eu/q_b/asn007/nloader/res/images/icon.png")));
			else primaryStage.getIcons().add(theme.THEME_ICON);
			ActionController.loginField.setText(config.getString("login"));
			ActionController.passField.setText(config.getString("pass"));
			ActionController.rememberMe.setSelected(!config.getString("login").equals("")); 
			BaseProcedures.log("Starting client verifier & downloader thread...", Main.class);
			ls = new LoadingServer();
			ActionController.servers.getItems().add(ls);
			ActionController.servers.setValue(ls);
			new InitBackgroundWorkerThread(true).start();
			BaseProcedures.log("Ready to rock!", Main.class);
			if(LauncherConf.useMusic) {
				    URL thing = getClass().getResource("/eu/q_b/asn007/nloader/res/music/" + LauncherConf.songFileName);
				    Media audioFile = new Media(thing.toString());                                       
				    player = new MediaPlayer(audioFile);
				    if(config.getBoolean("musicenabled")) {
				    	player.play();
				    	playing = true;
				    }
				    if(LauncherConf.loopMusic) player.setCycleCount(Integer.MAX_VALUE /* This should be enough, lol */);
				    ActionController.music.getStyleClass().add(config.getBoolean("musicenabled").toString());
			} else ActionController.removeMusicButton();
			primaryStage.show();
		} catch (IOException e) {BaseProcedures.log(BaseProcedures.stack2string(e), Main.class);}
		this.primaryStage = primaryStage;
	}

	private void initThemingSystem() throws Exception {
		if(LauncherConf.useOnlineTheme) {
				BaseProcedures.log("Looking for a theme.xml...", getClass());
				if(!config.getString("theme").equals("") && !config.getString("theme").equals("default")) {
					File themeXML = new File(ThemeLoader.getDirectoryForTheme(config.getString("theme")) + File.separator + "theme.xml");
					if(!themeXML.exists())
						downloadThemeXML();
					else if(BaseProcedures.getContentLength(LauncherConf.themeRoot + "theme.xml") != themeXML.length())
						downloadThemeXML();
				} else {
					downloadThemeXML();
				}

		} else config.setString("theme", "default");
		loadTheme(config.getString("theme"));
	}

	private String downloadThemeXML() {
		String s = BaseProcedures.runGET(LauncherConf.themeRoot + "theme.xml", "");
		if(s != null && !s.equals("")) {
			String name = ThemeLoader.getThemeName(s);
			BaseProcedures.writeString(s, new File(ThemeLoader.getDirectoryForTheme(name) + File.separator + "theme.xml"));
			config.setString("theme", name);
			return name;
		} else {
			BaseProcedures.log("Failed to locate theme.xml! Rolling back to default theme...", getClass());
			config.setString("theme", "default");
			return "unknown";
		}
	}

	private void loadTheme(String thm) {
		if(thm.equals("default")) {
			innerTheme = true;
			BaseProcedures.log("No external theme found, so using an inner theme", getClass());
		} else {
			innerTheme = false;
			BaseProcedures.log("Loading theme named " + thm, getClass());
			theme = ThemeLoader.loadTheme(ThemeLoader.getDirectoryForTheme(thm));
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public boolean isInnerTheme() {
		return innerTheme;
	}

	public void setInnerTheme(boolean innerTheme) {
		this.innerTheme = innerTheme;
	}
}
