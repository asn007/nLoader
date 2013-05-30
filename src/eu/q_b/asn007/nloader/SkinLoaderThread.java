package eu.q_b.asn007.nloader;

import java.awt.image.BufferedImage;
import java.io.File;

import javafx.application.Platform;

import javax.imageio.ImageIO;

public class SkinLoaderThread extends Thread {

	private String user;
	
	public SkinLoaderThread(String user) {
		Main._instance.launcherBusy = true;
		this.user = user;
	}
	
	public void run() {
		Platform.runLater(new Runnable(){
			public void run() {
				SkinController.loadingSkin.setVisible(true);
				SkinController.skinView.setStyle("");
			//	SkinController.skinView.setStyle("-fx-background-image: none;");		
			}
		});
		String skinURL = BaseProcedures.runGET(LauncherConf.skinURLScript, "act=url&user=" + user);
		if(skinURL == null) return; // Нас наебали, расходимся
		else if(skinURL.startsWith("http://")){
			try {
				final File skinFile = new File(BaseProcedures.getWorkingDirectory() + File.separator + "nloader-skins" + File.separator + user + ".png");
				BaseProcedures.download(BaseProcedures.toURL(skinURL), skinFile);
				BufferedImage img = ImageIO.read(skinFile);
				BufferedImage newSkin = BaseProcedures.buildSkinImage(img);
				ImageIO.write(newSkin, "png", skinFile);
				BaseProcedures.log("Done processing skin, setting up element styles...", getClass());
				Platform.runLater(new Runnable(){
					public void run() {
						SkinController.loadingSkin.setVisible(false);
						SkinController.skinView.setStyle("-fx-background-repeat: no-repeat;-fx-background-position: center;-fx-background-image:url('" + skinFile.toURI() + "');");		
					}
				});
				
			} catch(Exception ex) {
				ex.printStackTrace();
				// Да что ж за день-то сегодня такой...
			}	
		}
		Main._instance.launcherBusy = false;
	}
	
}
