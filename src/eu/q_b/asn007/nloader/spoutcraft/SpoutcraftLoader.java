package eu.q_b.asn007.nloader.spoutcraft;

import java.applet.Applet;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javafx.application.Platform;

import javax.swing.JFrame;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.fx.ModalWindow;

public class SpoutcraftLoader extends JFrame implements WindowListener {

	private static final long serialVersionUID = 5935951667321189765L;
	private MinecraftAppletEnglober minecraft;
	
	public SpoutcraftLoader(String user, String session) {
		this.setTitle("Spoutcraft");
		this.setBounds(0, 0, 1000, 600);
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		runGame(user, session, "");
	}

	public void runGame(String user, String session, String downloadTicket) {
		this.setLocationRelativeTo(null);
		Applet applet = null;
			applet = MinecraftLauncher.getMinecraftApplet(BaseProcedures.getLibraries());

		if (applet == null) {
			
			this.setVisible(false);
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					new ModalWindow(Main.loc.getString("nloader.generic.oops"), "Failed to launch SpoutCraft!");
					
				}
				
			});
			this.dispose();
			return;
		}

		minecraft = new MinecraftAppletEnglober(applet);
		minecraft.addParameter("username", user);
		minecraft.addParameter("sessionid", session);
		minecraft.addParameter("downloadticket", downloadTicket);
		minecraft.addParameter("spoutcraftlauncher", "true");
		//minecraft.addParameter("fullscreen", WindowMode.getModeById(Settings.getWindowModeId()) == WindowMode.FULL_SCREEN ? "true" : "false");

		applet.setStub(minecraft);
		this.add(minecraft);

		validate();
		this.setVisible(true);
		//TODO Incredibly hacky fix for grey screens due to Minecraft not making the resources directory.
		//TODO Someone fix this better (or not, no harm as it just works)
		new File(BaseProcedures.getWorkingDirectory(), "resources").mkdirs();
		minecraft.init();
		minecraft.setSize(getWidth(), getHeight());
		minecraft.start();
		return;
	}

	
	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		if (this.minecraft != null) {
			this.minecraft.stop();
			this.minecraft.destroy();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) { }
		}
		System.out.println("Exiting Spoutcraft");
		this.dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
