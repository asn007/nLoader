package eu.q_b.asn007.nloader.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.fx.SceneUtils;
import eu.q_b.asn007.nloader.theming.ThemeLoader;
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
		try {
			Parent p = FXMLLoader.load(ThemeLoader.getFXMLFromTheme(Main.theme, "MainScene"), Main.loc );
			p.getStylesheets().add(ThemeLoader.getStyleSheetFromTheme(Main.theme));
			ActionController.loginField.setText(Main._instance.config.getString("login"));
			ActionController.passField.setText(Main._instance.config.getString("pass"));
			ActionController.rememberMe.setSelected(!Main._instance.config.getString("login").equals("")); 
			SceneUtils.changeScene(500, th, p);
			new InitBackgroundWorkerThread(Main._instance.forceUpdate).start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
