package eu.q_b.asn007.nloader;



public class LauncherConf {

	public static final String mcDir = ".nloader";
	public static final String logFileName = "nloader.log";
	public static final String downloadURL = "http://minecraft.q-b.eu/mc/"; // Don't forget '/' on the end!
	public static final String authURL = "http://minecraft.q-b.eu/MineCraft/auth.php";
	
	public static final boolean useSkins = true;
	
	public static final String launcherVersion = "13";
	public static final String skinURLScript = "http://minecraft.q-b.eu/mc/skin.php";
	
	public static final String serverXMLURL = "http://minecraft.q-b.eu/mc/servers.xml";
	
	public static boolean isSpoutCraft = false;
	public static final String songFileName = "bg.mp3";
	public static final String themeRoot = "http://minecraft.q-b.eu/mc/example_theme/";
	
	public static final boolean useOnlineTheme = true;
	
	public static final String nloaderConfiguration = "nLoader.conf";
	public static final boolean useMusic = true;
	public static final boolean loopMusic = true;

	public static final boolean commandLaunch = true; // WARNING: BETA AND UNTESTED, SET TO TRUE ONLY WHEN YOUR CLIENT REFUSES TO LAUNCH, OTHERWISE SET TO FALSE (Can't be used with Spoutcraft)
	
	public static final boolean autoUpdate = true;
	public static final String baseFileURL = "http://minecraft.q-b.eu/mc/updater/latest"; // Without ".jar" or ".exe"
	
	public static final int WINDOWS_BAR_HEIGHT = 28; // Just in case
	public static final int BORDERS_WIDTH = 6;
	public static final int SCENE_WIDTH = 299;
	public static final int SCENE_HEIGHT = 452;
	
}
