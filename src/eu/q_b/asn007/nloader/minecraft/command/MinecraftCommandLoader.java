package eu.q_b.asn007.nloader.minecraft.command;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.helpers.command.JavaProcess;
import eu.q_b.asn007.nloader.helpers.command.JavaProcessLauncher;
import eu.q_b.asn007.nloader.helpers.command.JavaProcessRunnable;
import eu.q_b.asn007.nloader.multiclient.GameServer;

public class MinecraftCommandLoader implements JavaProcessRunnable {

	private final Object lock = new Object();
	private boolean isWorking = false;
	private File nativeDir;
	private String user;
	private String session;
	
	public MinecraftCommandLoader(String user, String session) {
		this.user = user;
		this.session = session;
		playGame();
	}

	   private void setWorking(boolean working) {
		  this.isWorking = working;
	   }
		 
	   public boolean isWorking() {
		 return this.isWorking;
	   }
		 
		   public void playGame() {
		     synchronized (this.lock) {
		       if (this.isWorking) {
		         BaseProcedures.log("Tried to play game but game is already starting!", this.getClass());
		         return;
		       }
		       setWorking(true);
		       launchGame();
		     }
		   }
		 
		   protected void launchGame() {
			 BaseProcedures.log("Launching minecraft", this.getClass());
			 GameServer selectedProfile = Main._instance.currentServer;
		 
		     this.nativeDir = new File(BaseProcedures.getWorkingDirectoryFor(selectedProfile), "bin" + File.separator + "natives");
		     if (!this.nativeDir.isDirectory()) this.nativeDir.mkdirs();
		     File gameDirectory = BaseProcedures.getWorkingDirectoryFor(selectedProfile);
		     BaseProcedures.log("Launching in " + gameDirectory, getClass());
		     JavaProcessLauncher processLauncher = new JavaProcessLauncher(BaseProcedures.getJavaExecutable(), new String[0]);
		     processLauncher.directory(gameDirectory);		 
		     processLauncher.addCommands(new String[] { "-Xmx" + Main._instance.config.getInteger("memory", 512) + "M" });
		     processLauncher.addCommands(new String[] { "-Dnet.minecraft.applet.TargetDirectory=" + BaseProcedures.getWorkingDirectoryFor(selectedProfile).getAbsolutePath()}); // This should work for directory changing, but I'm not sure
		     processLauncher.addCommands(new String[] { "-Djava.library.path=" + JavaProcessLauncher.escapeArgument(this.nativeDir.getAbsolutePath()) });
		     processLauncher.addCommands(new String[] { "-cp", constructClassPath(selectedProfile) });
		     processLauncher.addCommands(new String[] { "net.minecraft.client.Minecraft" }); 
		     processLauncher.addCommands(new String[] { user, session }); // TODO: Make it use every format, from Legacy to USER_SESSION_UUID
		     processLauncher.addCommands(new String[] { "--workDir", gameDirectory.getAbsolutePath() });
		     try
		     {
		       //String command = JavaProcessLauncher.buildCommands(processLauncher.getFullCommands());
		       //BaseProcedures.log("Running: " + command, this.getClass());
		       JavaProcess process = processLauncher.start();
		       process.safeSetExitRunnable(this);
		     } catch (IOException e) {
		       BaseProcedures.log("Couldn't launch game, stacktrace:\n" + BaseProcedures.stack2string(e), this.getClass());
		       setWorking(false);
		       return;
		     }
		   }
		 
		   private String constructClassPath(GameServer server)
		   {
		     StringBuilder result = new StringBuilder();
		     Collection<File> classPath = BaseProcedures.getClassPath(server);
		     String separator = System.getProperty("path.separator");
		 
		     for (File file : classPath) {
		       if (!file.isFile()) throw new RuntimeException("Classpath file not found: " + file);
		       if (result.length() > 0) result.append(separator);
		       result.append(file.getAbsolutePath());
		     }
		 
		     return result.toString();
		   }
		 
		   public void onJavaProcessEnded(JavaProcess process)
		   {
		     int exitCode = process.getExitCode();
		 
		     if (exitCode == 0) {
		       BaseProcedures.log("Game ended with no troubles detected (exit code " + exitCode + ")", this.getClass());
		     } else {
		    	 BaseProcedures.log("Game ended with bad state (exit code " + exitCode + ")", this.getClass());
		 
		       String errorText = null;
		       String[] sysOut = (String[])process.getSysOutLines().getItems();
		 
		       for (int i = sysOut.length - 1; i >= 0; i--) {
		         String line = sysOut[i];
		         String crashIdentifier = "#@!@#";
		         int pos = line.lastIndexOf(crashIdentifier);
		 
		         if ((pos >= 0) && (pos < line.length() - crashIdentifier.length() - 1)) {
		           errorText = line.substring(pos + crashIdentifier.length()).trim();
		           break;
		         }
		       }
		 
		       if(errorText != null) BaseProcedures.writeString(errorText, new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer) + File.separator + "minecraft-crash.log"));
		       setWorking(false);
		     }
		   }
		 
		   public void onReadyToLaunch()
		   {
		     synchronized (this.lock) {
		         if (isWorking()) {
		           try {
		             launchGame();
		           } catch (Throwable ex) {
		             BaseProcedures.log("LAUNCH ERROR: " + ex.getMessage() , this.getClass());
		           }
		       }
		     }
		   }	  
}
