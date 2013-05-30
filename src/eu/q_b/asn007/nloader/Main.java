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
import javafx.stage.Stage;

import eu.q_b.asn007.nloader.multiclient.GameServer;
import eu.q_b.asn007.nloader.multiclient.LoadingServer;
import eu.q_b.asn007.nloader.controllers.ActionController;
import eu.q_b.asn007.nloader.threading.*;
import eu.q_b.asn007.nloader.helpers.NLoaderConfiguration;

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
	
	@Override
	public void start(Stage primaryStage) {
		BaseProcedures.log("Initializing...", Main.class);
		System.setProperty("minecraft.applet.WrapperClass",
				"eu.q_b.asn007.nloader.minecraft.Launcher"); // Fuck Forge
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
			primaryStage.show();
			primaryStage.setWidth(305);
			primaryStage.setHeight(480);
			primaryStage.setResizable(false);
			primaryStage.setTitle(loc.getString("nloader.window.main.title"));
			ActionController.loginField.setText(config.getString("login"));
			ActionController.passField.setText(config.getString("pass"));
			BaseProcedures.log("Starting client verifier & downloader thread...", Main.class);
			ls = new LoadingServer();
			ActionController.servers.getItems().add(ls);
			ActionController.servers.setValue(ls);
			new InitBackgroundWorkerThread(true).start();
			BaseProcedures.log("Ready to rock!", Main.class);
		} catch (IOException e) {BaseProcedures.log(BaseProcedures.stack2string(e), Main.class);}
		this.primaryStage = primaryStage;
	}

	public static void main(String[] args) {
		BaseProcedures.log("*** nLoader ***", Main.class);
		launch(args);
	}
}
