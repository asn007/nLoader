package eu.q_b.asn007.nloader.skins;

import java.awt.Dimension;
import java.io.File;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.LauncherConf;
import eu.q_b.asn007.nloader.Main;

import javafx.stage.FileChooser;

public class SkinUploaderThread extends Thread {
	
	public SkinUploaderThread() {}
	
	public void run() {
		FileChooser skinChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
		skinChooser.getExtensionFilters().add(extFilter);
		File skinFile = skinChooser.showOpenDialog(Main._instance.primaryStage);
		if(skinFile != null) {
			Dimension dm = BaseProcedures.getImageDimension(skinFile.toString());
			if(dm == null || dm.getWidth() != 64 || dm.getHeight() != 32) {
				BaseProcedures.log("Not an image file!", getClass());
			} else {
				BaseProcedures.log("Image detected, uploading",  this.getClass());
				try {
					String s = BaseProcedures.runPostFile(BaseProcedures.toURL(LauncherConf.skinURLScript + "?act=upload"), skinFile, "user=" + Main.login);
					if(s.equals("OK")) new SkinLoaderThread(Main.login).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
