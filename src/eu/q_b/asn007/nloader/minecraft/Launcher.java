package eu.q_b.asn007.nloader.minecraft;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class Launcher extends Applet implements AppletStub {

	private static final long serialVersionUID = 1L;
	private Applet mcApplet = null;
	public Map<String, String> customParameters = new HashMap<String, String>();
	private int context = 0;
	private boolean active = false;
	private URL[] urls;
	private String binpath;
	private Container parent;
	
	public Launcher(String binpath, URL[] urls, MinecraftLoader minecraftLoader) {
		this.binpath = binpath;
		this.urls = urls;
		this.parent = minecraftLoader;
	}

	@Override
	public void appletResize(int paramInt1, int paramInt2) {
	}

	@Override
	public void init() {

		if (this.mcApplet != null && this.parent != null) {
			this.mcApplet.init();
			return;
		}

	}

	public void init(String userName, String sessionId) {
		@SuppressWarnings("resource")
		URLClassLoader cl = new URLClassLoader(this.urls);

		System.setProperty("org.lwjgl.librarypath", this.binpath
				+ File.separator + "natives");
		System.setProperty("net.java.games.input.librarypath", this.binpath
				+ File.separator + "natives");
		System.out.println("Loading minecraft applet...");
		try {

			Class<?> Mine = cl
					.loadClass("net.minecraft.client.MinecraftApplet");
			Applet applet = (Applet) Mine.newInstance();
			System.out.println("Success!");
			System.out.println("Setting applet size...");
			applet.setStub(this);
			applet.setSize(this.getWidth(), this.getHeight());
			System.out.println("Adding applet to canvas...");
			this.mcApplet = applet;
			setLayout(new BorderLayout());
			add(mcApplet, "Center");
			System.out.println("Starting applet...");
			init();
			System.out.println("Applet loaded!");
			this.active = true;
			validate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	@Override
	public String getParameter(String name) {
		String custom = (String) this.customParameters.get(name);
		if (custom != null)
			return custom;
		try {
			return super.getParameter(name);
		} catch (Exception e) {
			this.customParameters.put(name, null);
		}
		return null;
	}

	@Override
	public void start() {
		if (this.mcApplet != null) {
			this.mcApplet.start();
			return;
		}

	}

	@Override
	public boolean isActive() {
		if (context == 0) {
			context = -1;
			try {
				if (getAppletContext() != null)
					context = 1;
			} catch (Exception e) {
			}
		}
		if (context == -1)
			return active;
		return super.isActive();
	}

	@Override
	public URL getDocumentBase() {
		try {
			return new URL("http://minecraft.net/play");
		} catch (MalformedURLException e) {

		}
		return null;
	}

	@Override
	public void stop() {
		if (this.mcApplet != null) {
			this.active = false;
			this.mcApplet.stop();
			return;
		}
	}

	@Override
	public void destroy() {
		if (this.mcApplet != null) {
			this.mcApplet.destroy();
			return;
		}
	}

	public void replace(Applet paramApplet) {
		this.mcApplet = paramApplet;
		this.stop();
		paramApplet.setStub(this);
		paramApplet.setSize(getWidth(), getHeight());
		setLayout(new BorderLayout());
		add(paramApplet, "Center");
		paramApplet.init();
		this.active = true;
		paramApplet.start();
		validate();
	}

}
