package eu.q_b.asn007.nloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import eu.q_b.asn007.nloader.fx.SceneUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

public class SettingsController {

	@FXML
	private Node th;
	
	@FXML
	private TextField memory;

	@FXML 
	private CheckBox forceUpdate;
	
	@FXML
	public void saveThisShit() {
		BaseProcedures.writeString(memory.getText(), new File(BaseProcedures.getWorkingDirectory() + File.separator + "configuration"));
		Main._instance.forceUpdate = forceUpdate.isSelected();
		URL uri = Main.class.getResource( "/MainScene.fxml" );
		try {
			Parent p = FXMLLoader.load( uri, Main.loc );
			p.getStylesheets().add("/metro.css");
			SceneUtils.changeScene(500, th, p);
			if(forceUpdate.isSelected()) new ClientDownloaderThread().start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
