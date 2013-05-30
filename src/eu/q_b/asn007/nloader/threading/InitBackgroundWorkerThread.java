package eu.q_b.asn007.nloader.threading;

import java.io.File;

import javafx.application.Platform;

import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.LauncherConf;
import eu.q_b.asn007.nloader.multiclient.*;
import eu.q_b.asn007.nloader.controllers.ActionController;

public class InitBackgroundWorkerThread extends Thread {

	private boolean shouldStartVerify;
	
	public InitBackgroundWorkerThread(boolean shouldStartVerify) {
		this.shouldStartVerify = shouldStartVerify;
		Main._instance.launcherBusy = true;
	}
	
	public void run() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {ActionController.servers.getItems().clear();}
		});
		
		File serversXML = new File(BaseProcedures.getWorkingDirectory() + File.separator + "nloader-servers.xml");
		try {
			BaseProcedures.download(BaseProcedures.toURL(LauncherConf.serverXMLURL), serversXML);
			GameServerXMLParser parser = new GameServerXMLParser();
			parser.parseXML(serversXML);
			Main._instance.servers = parser.parseDocument("server");
			Platform.runLater(new Runnable(){
				public void run() {
					ActionController.servers.getItems().remove(Main._instance.ls);
				}
			});
			for(final GameServer gs: Main._instance.servers) {
				Platform.runLater(new Runnable(){
					public void run() {
						ActionController.servers.getItems().add(gs);
						Main._instance.currentServer = gs;
						ActionController.servers.setValue(gs);
					}
				});
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		Main._instance.launcherBusy = false;
		if(shouldStartVerify)
			new ClientDownloaderThread().start();
	}
	
}
