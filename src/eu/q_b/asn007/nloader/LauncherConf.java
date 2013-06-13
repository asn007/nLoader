package eu.q_b.asn007.nloader;



public class LauncherConf {

	public static final boolean devEnv = false; // CHANGE IT TO FALSE WHILE COMPILING, BACK TO TRUE WHEN TESTING IN IDE! IMPORTANT!
	
	public static final String mcDir = ".nloader";
	public static final String logFileName = "nloader.log";
	public static final String nloaderConfiguration = "nLoader.conf";
	
	public static final String serverXMLURL = "http://minecraft.q-b.eu/mc/servers.xml";
	public static final String downloadURL = "http://minecraft.q-b.eu/mc/"; // Don't forget '/' on the end!
	public static final String authURL = "http://minecraft.q-b.eu/MineCraft/auth.php";

	public static final String launcherVersion = "2.7";
	
	public static final boolean useSkins = true;
	public static final String skinURLScript = "http://minecraft.q-b.eu/mc/skin.php";
		
	public static final String songFileName = "bg.mp3";
	public static final boolean useMusic = true;
	public static final boolean loopMusic = true;
	
	public static final LaunchType minecraftLaunchType = LaunchType.MCMAIN; // Possible values: COMMAND, APPLET, or MCMAIN. I don't recommend using COMMAND one, the same applies to APPLET. They're deprecated, but still remain here for compatibility
	
	public static final boolean autoUpdate = true;
	public static final String baseFileURL = "http://minecraft.q-b.eu/mc/updater/latest"; // Without ".jar" or ".exe"
	
	public static final String themeRoot = "http://minecraft.q-b.eu/mc/example_theme/";
	public static final boolean useOnlineTheme = true;
	
	public static final int WINDOWS_BAR_HEIGHT = 28;
	public static final int BORDERS_WIDTH = 6;
	public static final int SCENE_WIDTH = 299;
	public static final int SCENE_HEIGHT = 452;
	
}
