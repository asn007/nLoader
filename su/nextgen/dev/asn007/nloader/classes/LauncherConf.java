package su.nextgen.dev.asn007.nloader.classes;

public class LauncherConf {
	
	/**															Строковые переменные														**/
	public static String minecraftDir = ".minecraft";
	
	public static String authURL = "http://dev.nextgen.su/demo/nloader-min/mcr/MineCraft/auth.php";
	public static String updateSite = "http://dev.nextgen.su/demo/nloader-min/files/";
	public static String defaultUsername = "nGuest";
	public static String serverIP = "localhost";
	public static String serverPort = "25565";
	public static String launcherName = "nLoader 1.0.1";
	public static String launcherVersion = "13";
	public static String newLauncherPage = "http://nextgen.su/nex-launcher.html";
	public static String titleInGame = "NeXTGeN: nV";

	/**															Boolean (true-false)														**/
	public static boolean autoConnect = true;
	public static boolean removeMods = true;
	public static boolean useRootZip = true;
	
	/******************************************************************************************************************************
	 * 																															  *
	 * 												Краткое описание настроек nLoader											  *
	 * 																															  *
	 * 	Параметр		|				Описание							|  					Дефолтное значение 				  *
	 *					|													|									  				  *
	 *  minecraftDir	| Папка minecraft (можно с точкой, можно без)		| ".nextgen"		  								  *
	 *  removeMods		| Удалять ли моды при запуске						| true			  				  				  	  *
	 * 	authURL			| URL аутентификации								| "http://nextgen.su/login20/auth.php"				  *
	 * 	updateSite      | URL обновления ("/" на конце обязятелен!)			| "http://dev.nextgen.su/demo/nloader-min/files/"	  *	
	 * 	defaultUsername | Имя пользователя в оффлайн-режиме					| "nGuest"											  *
	 *  serverIP		| IP сервера										| "localhost"										  *
	 * 	serverPort		| Порт сервера										| "25565"											  *
	 * 	autoConnect		| Подключаться ли автоматически?					| true												  *
	 * 	launcherName	| Имя окна лаунчера									| "nLoader 1.0"										  *
	 * 	launcherVersion | Версия лаунчера									| "10"												  *
	 * 	newLauncherPage	| Страница, где можно обновить лаунчер				| "http://nextgen.su/nex-launcher.html"				  *
	 * 	titleInGame		| Имя окна игры										| "NeXTGeN nV"										  *	
	 * 	useRootZip		| Использовать ли root.zip							| true											      *
	 * ****************************************************************************************************************************/
}
