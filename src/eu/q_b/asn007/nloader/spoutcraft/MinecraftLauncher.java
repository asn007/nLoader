package eu.q_b.asn007.nloader.spoutcraft;

import java.applet.Applet;
import java.io.File;
import java.util.List;

import eu.q_b.asn007.nloader.helpers.Library;
import eu.q_b.asn007.nloader.*;

public class MinecraftLauncher {
	private static MinecraftClassLoader loader = null;
	public static MinecraftClassLoader getClassLoader(List<Library> libraries) {
		if (loader == null) {
			File mcBinFolder = new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer), "bin");

			File spoutcraftJar = new File(mcBinFolder, "spoutcraft.jar");
			File minecraftJar = new File(mcBinFolder, "minecraft.jar");
			File jinputJar = new File(mcBinFolder, "jinput.jar");
			File lwglJar = new File(mcBinFolder, "lwjgl.jar");
			File lwjgl_utilJar = new File(mcBinFolder, "lwjgl_util.jar");

			File[] files = new File[4 + libraries.size()];

			int index = 0;
			for (Library lib : libraries) {
				File libraryFile = new File(mcBinFolder, "lib" + File.separator + lib.name() + ".jar");
				files[index] = libraryFile;
				index++;
			}

			try {
				files[index + 0] = minecraftJar;
				files[index + 1] = jinputJar;
				files[index + 2] = lwglJar;
				files[index + 3] = lwjgl_utilJar;

				loader = new MinecraftClassLoader(ClassLoader.getSystemClassLoader(), spoutcraftJar, files);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return loader;
	}

	public static void resetClassLoader() {
		loader = null;
	}

	@SuppressWarnings("rawtypes")
	public static Applet getMinecraftApplet(List<Library> libraries)  {
		File mcBinFolder = new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer), "bin");

		try {
			ClassLoader classLoader = getClassLoader(libraries);

			String nativesPath = new File(mcBinFolder, "natives").getAbsolutePath();
			System.setProperty("org.lwjgl.librarypath", nativesPath);
			System.setProperty("net.java.games.input.librarypath", nativesPath);
			System.setProperty("org.lwjgl.util.Debug", "true");
			System.setProperty("org.lwjgl.util.NoChecks", "false");

			Class minecraftClass = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
			return (Applet) minecraftClass.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}

}
