package eu.q_b.asn007.nloader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javafx.application.Platform;
public class ClientDownloaderThread extends Thread {
	
	private HashMap<URL, File> filesToGet = new HashMap<URL, File>();
	
	public ClientDownloaderThread() {
		Main._instance.launcherBusy = true;
	}
	
	public void run() {
		Platform.runLater(new Runnable(){
			public void run() {
				ActionController.downloadStatus.setText(Main.loc.getString("nloader.window.main.verifying"));
				ActionController.progressBar.setProgress(-1);
			}
		});
		BaseProcedures.log("Building directories list...", getClass());
		ArrayList<String> dirs = BaseProcedures.getDirectoriesList();
		BaseProcedures.log("Checking files...", getClass());
		for(String str: dirs) {
			File tmp = new File(BaseProcedures.getWorkingDirectory() + File.separator + str);
			if(tmp.exists()){
				List<File> files = BaseProcedures.addFiles(tmp);
				for(File f: files) {
					String s = BaseProcedures.runGET(LauncherConf.downloadURL + "verifier.php", "act=verify&file=" + RelativePathMaker.getRelativePath(BaseProcedures.getWorkingDirectory(), f).replace(File.separator, "/") + "&hash=" + BaseProcedures.getMD5(f));
					if(Main._instance.forceUpdate) f.delete();
					else if(s == null) f.delete();
					else if(s.contains("no")) f.delete();
				}
			}
			
		}
		BaseProcedures.log("File checking completed, populating map with files to download", getClass());
		String s = BaseProcedures.runGET(LauncherConf.downloadURL + "verifier.php", "act=list");
		String[] fileHashPairs = s.split("<br />");
		for(String fileHashPair: fileHashPairs) {
			String file = fileHashPair.split("<::::>")[0];
			String hash = fileHashPair.split("<::::>")[1];
			if(!BaseProcedures.getMD5(new File(BaseProcedures.getWorkingDirectory() + File.separator + file.replace("/", File.separator))).equals(hash)) filesToGet.put(BaseProcedures.toURL(LauncherConf.downloadURL + "/client/" + file), new File(BaseProcedures.getWorkingDirectory() + File.separator + file.replace("/", File.separator)));
		}
		BaseProcedures.log("Map populated, have to download " + filesToGet.size() + " files",  getClass());
		for(Entry<URL, File> entry: filesToGet.entrySet()) {
			try {
				BaseProcedures.download(entry.getKey(), entry.getValue(), ActionController.downloadStatus, ActionController.progressBar);
			} catch (Exception e) {
				BaseProcedures.log(BaseProcedures.stack2string(e), getClass());
			}
		}
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
