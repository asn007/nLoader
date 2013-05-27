package eu.q_b.asn007.nloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.application.Platform;
public class ClientDownloaderThread extends Thread {
	
	private HashMap<URL, File> files = new HashMap<URL, File>();
	
	private boolean natives = new File(BaseProcedures.getWorkingDirectory()
			+ File.separator + "bin" + File.separator + "natives").exists();
	public ClientDownloaderThread() {
		Main._instance.launcherBusy = true;
		if(!Main._instance.forceUpdate) {
			if(!natives)
				files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "natives.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "natives.zip"));
			if(!new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "minecraft.jar").exists()) 
				files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "minecraft.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "minecraft.jar"));
			if(!new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lwjgl.jar").exists())
				files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "lwjgl.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lwjgl.jar"));
			if(!new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lwjgl_util.jar").exists())
				files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "lwjgl_util.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lwjgl_util.jar"));
			if(!new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "jinput.jar").exists())
				files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "jinput.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "jinput.jar"));				
		} else {
			try {
				BaseProcedures.recursiveDelete(new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			natives = false;
			files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "natives.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "natives.zip"));
			files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "minecraft.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "minecraft.jar"));
			files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "lwjgl.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lwjgl.jar"));
			files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "lwjgl_util.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lwjgl_util.jar"));
			files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "jinput.jar"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "jinput.jar"));
			if(LauncherConf.useRootZip)
				files.put(BaseProcedures.toURL(LauncherConf.downloadURL + "root.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "root.zip"));
		}
		if(files.size() > 0)
			BaseProcedures.log(files.size() + " to download", this.getClass());
		else
			BaseProcedures.log("Nothing to download", this.getClass());
	}
	@SuppressWarnings("unused")
	public void run() {
		UnZipper uz = new UnZipper();
		Platform.runLater(new Runnable(){
			public void run() {
				ActionController.downloadStatus.setText(Main.loc.getString("nloader.window.main.todownload"));
				ActionController.progressBar.setProgress(0);
			}
		});
		for(Entry<URL, File> entry: files.entrySet()) {
			try {
				BaseProcedures.download(entry.getKey(), entry.getValue(), ActionController.downloadStatus, ActionController.progressBar);
			} catch(Exception e) {
				BaseProcedures.log("Error while downloading, stacktrace below:\n " + BaseProcedures.stack2string(e), getClass());
			}
		}
		if(!natives)			
			uz.recursiveUnzip(new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "natives.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "natives"));
		if(Main._instance.forceUpdate && LauncherConf.useRootZip)
			uz.recursiveUnzip(new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "natives.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator));
		uz.removeAllZipFiles(new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator));
		Platform.runLater(new Runnable(){
			public void run() {
				ActionController.downloadStatus.setText(Main.loc.getString("nloader.window.main.verified"));
				ActionController.progressBar.setVisible(false);
			}
		});
		System.gc();
		Main._instance.launcherBusy = false;
	}
}
