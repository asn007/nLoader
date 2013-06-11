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
	
	@Override
	public void start(Stage primaryStage) {
		BaseProcedures.log("Initializing...", Main.class);
		System.setProperty("minecraft.applet.WrapperClass",
				Launcher.class.getCanonicalName()); // Fuck Forge
		
		_instance = this;
		config = new NLoaderConfiguration(new File(BaseProcedures.getWorkingDirectory() + File.separator + LauncherConf.nloaderConfiguration));
		URL    uri = Main.class.getResource( "/MainScene.fxml" );
		try {
			BaseProcedures.log("Loading scene FXML...", Main.class);
			Parent p   = FXMLLoader.load(uri, loc);
			BaseProcedures.log("Adding stylesheets...", Main.class);
			p.getStylesheets().add("/metro.css");
			BaseProcedures.log("Setting up stage...", Main.class);
			primaryStage.setScene(new Scene(p));
			primaryStage.setWidth(305);
			primaryStage.setHeight(480);
			primaryStage.setResizable(false);
			primaryStage.setTitle(loc.getString("nloader.window.main.title"));
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/eu/q_b/asn007/nloader/res/images/icon.png")));
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

	public static void main(String[] args) {
		BaseProcedures.log("*** nLoader ***", Main.class);
		launch(args);
	}
}
