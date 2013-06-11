package eu.q_b.asn007.nloader.threading;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.LauncherConf;

public class LauncherUpdaterThread extends Thread {
	
	public void run() { 
		BaseProcedures.log("Starting launcher update process...", this.getClass());
		String thisJarPath = BaseProcedures.getMyJarName();
		BaseProcedures.log("Jar file: " + thisJarPath, getClass());
		BaseProcedures.log("Downloading fresh launcher...", this.getClass());
		String path = LauncherConf.baseFileURL +  ((thisJarPath.endsWith(".jar")) ? ".jar" : ".exe");
		try {
			BaseProcedures.download(BaseProcedures.toURL(path), new File(thisJarPath));
			doRelaunch();
		} catch (Exception e) {
			BaseProcedures.log("Failed to update launcher, stacktrace below...", this.getClass());
			BaseProcedures.log(BaseProcedures.stack2string(e), getClass());
		} 
	}

	private void doRelaunch() {
		BaseProcedures.log("Starting relaunch...", getClass());
		ArrayList<String> params = new ArrayList<String>();
		if(BaseProcedures.getMyJarName().endsWith(".jar")) {
			params.add(BaseProcedures.getJavaExecutable());
			params.add("-jar");
		}
		params.add(BaseProcedures.getMyJarName());
		ProcessBuilder pb = new ProcessBuilder(params);
		try {
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			BaseProcedures.log("Unable to relaunch!", getClass());
		}
		BaseProcedures.log("Stopping launcher...", getClass());
		System.exit(0);
	}

}
