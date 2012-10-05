package su.nextgen.dev.asn007.nloader.classes;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import su.nextgen.dev.asn007.nloader.classes.BaseProcedures.OS;
import su.nextgen.dev.asn007.nloader.gui.ProgressBar;

public class GameUpdater {
private static String gameBuild;	
public static HashMap<URL, File> fileList = new HashMap<URL, File>();
public static ProgressBar p;

	public GameUpdater(String gameBuild, ProgressBar p) {
		
		GameUpdater.p = p;
		GameUpdater.gameBuild = gameBuild;
		fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "minecraft.jar"), new File(BaseProcedures.getBinFolder()+ File.separator + "minecraft.jar"));
		fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "jinput.jar"), new File(BaseProcedures.getBinFolder()+ File.separator + "jinput.jar"));
		fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "lwjgl.jar"), new File(BaseProcedures.getBinFolder()+ File.separator + "lwjgl.jar"));
		fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "lwjgl_util.jar"), new File(BaseProcedures.getBinFolder()+ File.separator + "lwjgl_util.jar"));
		if(LauncherConf.useRootZip)
		fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "root.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator + "root.zip"));
		if(BaseProcedures.getPlatform() == OS.windows) {
			//ШINDOШS не нужна, но раз уж это самая популярная платформа...
			fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "windows_natives.zip"), new File(BaseProcedures.getNativesFolder()+ File.separator + "natives.zip"));
		}
		else if(BaseProcedures.getPlatform() == OS.linux) {
			fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "linux_natives.zip"), new File(BaseProcedures.getNativesFolder()+ File.separator + "natives.zip"));
		}
		else if(BaseProcedures.getPlatform() == OS.macos) {
			//<s>Макопидары идут нахуй</s>. Ой, простите, вырвалось...
			fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "mac_natives.zip"), new File(BaseProcedures.getNativesFolder()+ File.separator + "natives.zip"));
		}
		else if(BaseProcedures.getPlatform() == OS.solaris) {
			//<s>2.5 пользователя солярки тоже идут нахуй</s>. Ой, простите, снова вырвалось...
			fileList.put(BaseProcedures.toURL(LauncherConf.updateSite + "sol_natives.zip"), new File(BaseProcedures.getNativesFolder()+ File.separator + "natives.zip"));
		} else {
			System.exit(0); //А пусть сам догадается, почему лаунчер вдруг вылетел, ехехе :3
		}
		//И вообще, заебался я. Время позднее, пойду-ка посплю...
	}
	
	public void update() {
		NLoader.updatingLabel.setText("Обновление...");
		new Thread() {
			public void run() {
				BaseProcedures.doCleanUp();
				for(Entry<URL, File> e : fileList.entrySet()) {
					try {
						p.setValue(0);
						BaseLogger.write("Downloading file: " + e.getValue().getName());
						NLoader.updatingLabel.setText("<html>Обновление...<br />("+ e.getValue().getName() +")</html>");
						BaseProcedures.download(e.getKey(), e.getValue(), p);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						JOptionPane	.showMessageDialog(
								null,
								"Не удалось скачать файл " + e.getValue().getName() + ". Проверьте подключение к Интернету. Лаунчер будет закрыт",
								"FAILED TO DOWNLOAD FILE!",
								JOptionPane.ERROR_MESSAGE);
						BaseLogger.write("Error while downloading file: " + e.getValue().getName());
						BaseLogger.write(e1);
						System.exit(0);
						
					}
					
				}
				UnZipper uz = new UnZipper();
				uz.recursiveUnzip(new File(BaseProcedures.getNativesFolder() + File.separator + "natives.zip"), BaseProcedures.getNativesFolder());
				if(LauncherConf.useRootZip) {
				uz.recursiveUnzip(new File(BaseProcedures.getWorkingDirectory() + File.separator + "root.zip"), new File(BaseProcedures.getWorkingDirectory() + File.separator));
				uz.removeAllZipFiles(BaseProcedures.getWorkingDirectory());
				}
				uz.removeAllZipFiles(BaseProcedures.getNativesFolder());
				uz = null;
				System.gc();
				BaseProcedures.writeVersionFile(getGameBuild());
				BaseLogger.write("Launching game...");
				p.setValue(0);
				NLoader.updatingLabel.setText("Запуск");
				
				loadGame(NLoader.username, NLoader.session);
			}
		}.start();
	}
	
	public void loadGame(String user, String session) {
		NLoader.frame.setVisible(false);
		new MinecraftLoader(user, session);
	
	}
	
	
	
	public boolean shouldUpdate() {
		
		String build = BaseProcedures.readFileAsString(BaseProcedures.getBinFolder()+ File.separator + "build", "0");
		//BaseLogger.write(build); Used for debugging, now kinda useless
		if(!build.equals(getGameBuild()) || NLoader.mustReinstall || !BaseProcedures.isClientCorrect()) return true;
		else return false;
	}
	
	public String getGameBuild() {
		return GameUpdater.gameBuild;
	}
}
