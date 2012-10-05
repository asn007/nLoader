package su.nextgen.dev.asn007.nloader.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BgPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -461714527865425671L;

	public Image bgImage = null;

	public BgPanel(String resname) {
		MediaTracker mt = new MediaTracker(this);
		try {

			bgImage = ImageIO.read(BgPanel.class
					.getResource("/su/nextgen/dev/asn007/nloader/gui/files/"
							+ resname));
			bgImage.getWidth(null);
			bgImage.getHeight(null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block

		}
		mt.addImage(bgImage, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		bgImage.getWidth(null);
		bgImage.getHeight(null);
		g.drawImage(bgImage, 1, 1, null);
		// manySec(200);
		// repaint();
	}

	public static void manySec(int ms) {
		try {
			Thread.currentThread();
			Thread.sleep(ms / 10);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	public void setBG(Image i) {
		this.bgImage = i;
	}
}
