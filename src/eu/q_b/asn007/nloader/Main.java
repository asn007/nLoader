package eu.q_b.asn007.nloader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	
    public static final String MAIN_SCREEN = "MainScene";
    public static final String MAIN_SCREEN_FXML = "/MainScene.fxml";
    
    public static final String SETTINGS_SCREEN = "Settings";
    public static final String SETTINGS_SCREEN_FXML = "/Settings.fxml";
    
    public static final ResourceBundle loc = ResourceBundle.getBundle("nloader");
	public static Main _instance;
	public Stage primaryStage;
	
	public boolean forceUpdate = false;
	public boolean launcherBusy = false;
	
	@Override
	public void start(Stage primaryStage) {
		BaseProcedures.log("Initializing...", Main.class);
		_instance = this;
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
			primaryStage.setHeight(400);
			primaryStage.setResizable(false);
			BaseProcedures.log("Starting client verifier & downloader thread...", Main.class);
			((Thread) new ClientDownloaderThread()).start();
			BaseProcedures.log("Ready to go!", Main.class);
			
		} catch (IOException e) {BaseProcedures.log(BaseProcedures.stack2string(e), Main.class);}
		this.primaryStage = primaryStage;
	}

	public static void main(String[] args) {
		BaseProcedures.log("*** nLoader ***", Main.class);
		launch(args);
		System.setProperty("minecraft.applet.WrapperClass",
				"eu.q_b.asn007.nloader.Launcher"); // Fuck Forge
	}
}
