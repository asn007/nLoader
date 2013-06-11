package eu.q_b.asn007.nloader.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.BasicMinecraftLoader;
import eu.q_b.asn007.nloader.LauncherConf;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.fx.ModalWindow;
import eu.q_b.asn007.nloader.fx.SceneUtils;
import eu.q_b.asn007.nloader.multiclient.GameServer;
import eu.q_b.asn007.nloader.skins.SkinLoaderThread;
import eu.q_b.asn007.nloader.threading.LauncherUpdaterThread;
public class ActionController {

	@FXML private static Node th;
	@FXML public static Label downloadStatus;
	@FXML public static ProgressBar progressBar;
	@FXML public static TextField loginField;
	@FXML public static PasswordField passField;
	@FXML public static CheckBox rememberMe;
	@FXML public static ComboBox<GameServer> servers;
	@FXML public static Label music;
	
	@FXML
	public void openSettings(ActionEvent evt) {
		if(!Main._instance.launcherBusy) {
		URL uri = Main.class.getResource( "/SettingsScene.fxml" );
		try {
			Parent p = FXMLLoader.load( uri, Main.loc );
			p.getStylesheets().add("/metro.css");
			SceneUtils.changeScene(500, th, p);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		} else {
			new ModalWindow(Main.loc.getString("nloader.generic.oops"), Main.loc.getString("nloader.generic.bgwait"));
		}
		
	}


	@FXML public void doLogin() {
		// TODO: Make it run in separate Thread
				if(!Main._instance.launcherBusy) {
					String user = loginField.getText();
					String pass = passField.getText();
					String result = BaseProcedures.runPOST(LauncherConf.authURL, "user=" + user + "&password=" + pass + "&version=" + LauncherConf.launcherVersion);
					if(result==null) downloadStatus.setText("Could not connect to the server");
					else if(result.equals("Bad login")) downloadStatus.setText(Main.loc.getString("nloader.window.main.badlogin"));
					else if(result.equals("Old version")) {
						downloadStatus.setText(Main.loc.getString("nloader.window.main.oldversion"));
						if(LauncherConf.autoUpdate) {
							downloadStatus.setText(Main.loc.getString("nloader.window.main.autoupdate"));
							new LauncherUpdaterThread().start();
						}
					}
					else if(result.contains(":")) {
						String[] arr = result.split(":");
						Main.login = arr[2];
						Main.session = arr[3];
						if(rememberMe.isSelected()) {
							Main._instance.config.setString("login", loginField.getText());
							Main._instance.config.setString("pass", passField.getText());
						}
							
						if(!LauncherConf.useSkins)
							new BasicMinecraftLoader(arr[2], arr[3]);
						else {
							doSkinScene();
						}
					} else downloadStatus.setText(result);
					} else {
						new ModalWindow(Main.loc.getString("nloader.generic.oops"), Main.loc.getString("nloader.generic.bgwait"));
					}
	}


	private void doSkinScene() {
		URL uri = Main.class.getResource( "/SkinScene.fxml" );
		try {
			Parent p = FXMLLoader.load(uri, Main.loc);
			p.getStylesheets().add("/metro.css");
			SceneUtils.changeScene(500, th, p);
			new SkinLoaderThread(Main.login).start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}


	@FXML public void updateCombobox() {
		Main._instance.currentServer = servers.getValue();
	}


	@FXML public void switchMusicState() {
		if(Main._instance.playing && LauncherConf.useMusic) {
			Main._instance.player.pause();
			Main._instance.config.setBoolean("musicenabled", false);
			Main._instance.playing = false;
			music.getStyleClass().add("false");
			music.getStyleClass().remove("true");
		} else if(LauncherConf.useMusic) {
			Main._instance.player.play();
			Main._instance.config.setBoolean("musicenabled", true);
			Main._instance.playing = true;
			music.getStyleClass().remove("false");
			music.getStyleClass().add("true");
		}
	}


	public static void removeMusicButton() {
		music.getStyleClass().add("nothing");
	}
	
}
