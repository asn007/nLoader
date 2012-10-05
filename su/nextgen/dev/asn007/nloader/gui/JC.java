package su.nextgen.dev.asn007.nloader.gui;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

public class JC extends JCheckBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = -992577510391912476L;

	public JC() {
		super();
		this.setRolloverEnabled(false);
		setBorder(BorderFactory.createEmptyBorder());
		try {
			this.setIcon(new ImageIcon(
					ImageIO.read(JC.class
							.getResourceAsStream("/su/nextgen/dev/asn007/nloader/gui/files/chkbx.png"))));
			this.setSelectedIcon(new ImageIcon(
					ImageIO.read(JC.class
							.getResourceAsStream("/su/nextgen/dev/asn007/nloader/gui/files/chkbx_clk.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public JC(String s) {
		super(s);
		this.setRolloverEnabled(false);
		setBorder(BorderFactory.createEmptyBorder());
		try {
			this.setIcon(new ImageIcon(
					ImageIO.read(JC.class
							.getResourceAsStream("/su/nextgen/dev/asn007/nloader/gui/files/chkbx.png"))));
			this.setSelectedIcon(new ImageIcon(
					ImageIO.read(JC.class
							.getResourceAsStream("/su/nextgen/dev/asn007/nloader/gui/files/chkbx_clk.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public boolean isOpaque() {
		return false;
	}

}
