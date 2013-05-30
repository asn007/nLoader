package eu.q_b.asn007.nloader;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import eu.q_b.asn007.nloader.fx.ModalWindow;
import eu.q_b.asn007.nloader.fx.SceneUtils;
public class ActionController {

	@FXML private Node th;
	@FXML public static Label downloadStatus;
	@FXML public static ProgressBar progressBar;
	@FXML TextField loginField;
	@FXML PasswordField passField;
	
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
					else if(result.equals("Old version")) downloadStatus.setText(Main.loc.getString("nloader.window.main.oldversion"));
					else if(result.contains(":")) {
						String[] arr = result.split(":");
						Main.login = arr[2];
						Main.session = arr[3];
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
	
}
