package eu.q_b.asn007.nloader.minecraft.mcmain;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.Main;

public class MinecraftMainLoader {

	public MinecraftMainLoader(String user, String session) {
		URL[] urls = new URL[4];

		try {
			urls[0] = new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer) + File.separator + "bin" + File.separator + "minecraft.jar").toURI().toURL();
			urls[1] = new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer)
					+ File.separator + "bin" + File.separator, "lwjgl.jar")
					.toURI().toURL();
			urls[2] = new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer)
					+ File.separator + "bin" + File.separator, "jinput.jar")
					.toURI().toURL();
			urls[3] = new File(BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer)
					
					+ File.separator + "bin" + File.separator, "lwjgl_util.jar")
					.toURI().toURL();
		} catch(Exception e) {
			BaseProcedures.log(BaseProcedures.stack2string(e), getClass());
		}
		start(user, session, urls);
	}

	private void start(String user, String session, URL[] urls) {
		String binpath = BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer) + File.separator + "bin";
		@SuppressWarnings("resource")
		URLClassLoader cl = new URLClassLoader(urls);
		System.setProperty("org.lwjgl.librarypath", binpath
				+ File.separator + "natives");
		System.setProperty("net.java.games.input.librarypath", binpath
				+ File.separator + "natives");
		BaseProcedures.log("Loading minecraft main class...", getClass());
		try {
			Class<?> mc = cl.loadClass("net.minecraft.client.Minecraft");
			Method minecraftMain = mc.getMethod("main", String[].class);
			minecraftMain.invoke(new Object[]{}, new Object[] { new String[]{user, session} });
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
