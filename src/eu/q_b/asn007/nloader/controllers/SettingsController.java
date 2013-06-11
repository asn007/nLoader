package eu.q_b.asn007.nloader.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.fx.SceneUtils;
import eu.q_b.asn007.nloader.threading.InitBackgroundWorkerThread;

public class SettingsController {

	@FXML
	private Node th;
	
	@FXML
	private TextField memory;

	@FXML 
	private CheckBox forceUpdate;
	
	@FXML
	public void saveThisShit() {
		Main._instance.config.setString("memory", memory.getText());
		Main._instance.forceUpdate = forceUpdate.isSelected();
		URL uri = Main.class.getResource( "/MainScene.fxml" );
		try {
			Parent p = FXMLLoader.load( uri, Main.loc );
			p.getStylesheets().add("/metro.css");
			ActionController.loginField.setText(Main._instance.config.getString("login"));
			ActionController.passField.setText(Main._instance.config.getString("pass"));
			SceneUtils.changeScene(500, th, p);
			new InitBackgroundWorkerThread(Main._instance.forceUpdate).start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
