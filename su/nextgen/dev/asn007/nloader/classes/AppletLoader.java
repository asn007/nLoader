package su.nextgen.dev.asn007.nloader.classes;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class AppletLoader extends Applet implements Runnable, AppletStub,
		MouseListener {

	private static final long serialVersionUID = 1L;
	private Applet mcApplet = null;
	public Map<String, String> customParameters = new HashMap<String, String>();
	private int context = 0;
	private boolean active = false;
	private URL[] urls;
	private String binpath;

	public AppletLoader(String binpath, URL[] urls) {
		this.binpath = binpath;
		this.urls = urls;
	}

	@Override
	public void mouseClicked(MouseEvent paramMouseEvent) {/**/
	}

	@Override
	public void mousePressed(MouseEvent paramMouseEvent) {/**/
	}

	@Override
	public void mouseReleased(MouseEvent paramMouseEvent) {/**/
	}

	@Override
	public void mouseEntered(MouseEvent paramMouseEvent) {/**/
	}

	@Override
	public void mouseExited(MouseEvent paramMouseEvent) {/**/
	}

	@Override
	public void appletResize(int paramInt1, int paramInt2) {/**/
	}

	@Override
	public void run() {/**/
	}

	@Override
	public void init() {

		if (this.mcApplet != null) {
			this.mcApplet.init();
			return;
		}

	}

	public void init(String userName, String sessionId) {
		URLClassLoader cl = new URLClassLoader(this.urls);

		System.setProperty("org.lwjgl.librarypath", this.binpath
				+ File.separator + "natives");
		System.setProperty("net.java.games.input.librarypath", this.binpath
				+ File.separator + "natives");
		BaseLogger.write("Loading minecraft applet...");
		try {

			Class<?> Mine = cl
					.loadClass("net.minecraft.client.MinecraftApplet");
			Applet applet = (Applet) Mine.newInstance();
			BaseLogger.write("Success!");
			BaseLogger.write("Setting applet size...");
			applet.setStub(this);
			applet.setSize(this.getWidth(), this.getHeight());
			BaseLogger.write("Adding applet to canvas...");
			this.mcApplet = applet;
			setLayout(new BorderLayout());
			add(mcApplet, "Center");
			BaseLogger.write("Starting applet...");
			init();
			BaseLogger.write("Applet loaded!");
			this.active = true;
			validate();
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.write(e);
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
			} catch (Exception e) {/**/
			}
		}
		if (context == -1)
			return active;
		return super.isActive();
	}

	@Override
	public URL getDocumentBase() {
		try {
			return new URL("http://nextgen.su/play.html");
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

}
