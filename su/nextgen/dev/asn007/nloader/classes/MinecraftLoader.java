package su.nextgen.dev.asn007.nloader.classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

public class MinecraftLoader extends JFrame {

	private static final long serialVersionUID = -2287752224571892380L;

	public MinecraftLoader(String u, String s) {

		URL[] urls = new URL[4];

		try {
			urls[0] = new File(BaseProcedures.getBinFolder(), "minecraft.jar")
					.toURI().toURL();
			urls[1] = new File(BaseProcedures.getBinFolder(), "lwjgl.jar")
					.toURI().toURL();
			urls[2] = new File(BaseProcedures.getBinFolder(), "jinput.jar")
					.toURI().toURL();
			urls[3] = new File(BaseProcedures.getBinFolder(), "lwjgl_util.jar")
					.toURI().toURL();
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.write(e);
			System.exit(0);
		}

		final AppletLoader gameapplet = new AppletLoader(BaseProcedures
				.getBinFolder().toString(), urls);
		gameapplet.customParameters.put("username", u);
		gameapplet.customParameters.put("sessionid", s);
		gameapplet.customParameters.put("stand-alone", "true");

		if (LauncherConf.autoConnect) {
			gameapplet.customParameters.put("server", LauncherConf.serverIP);
			gameapplet.customParameters.put("port", LauncherConf.serverPort);
		}
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				BaseLogger.write("Stopping game and exiting...");
				gameapplet.stop();
				gameapplet.destroy();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		});
		this.setTitle(LauncherConf.titleInGame);

		this.setBounds(0, 0, 640, 480);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.setIconImage(NLoader.frame.getIconImage());
		this.add(gameapplet, "Center");
		validate();
		this.setVisible(true);
		gameapplet.init(u, s);
		gameapplet.start();

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

	}
}