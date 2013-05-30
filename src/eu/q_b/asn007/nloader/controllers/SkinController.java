package eu.q_b.asn007.nloader.controllers;

import eu.q_b.asn007.nloader.BasicMinecraftLoader;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.fx.ModalWindow;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.ProgressIndicator;
import eu.q_b.asn007.nloader.skins.*;

public class SkinController {

	@FXML Pane th;
	@FXML public static Pane skinView;
	@FXML public static ProgressIndicator loadingSkin;
	


	@FXML public void doLaunch() {
		new BasicMinecraftLoader(Main.login, Main.session);
	}

	@FXML public void doChangeSkin() {
		if(Main._instance.launcherBusy) new ModalWindow(Main.loc.getString("nloader.generic.oops"), Main.loc.getString("nloader.generic.bgwait"));
		else new SkinUploaderThread().start();
	}

}
