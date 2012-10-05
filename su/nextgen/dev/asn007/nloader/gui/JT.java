package su.nextgen.dev.asn007.nloader.gui;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class JT extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5797737275533384557L;

	public JT() {
		super();
		setBorder(BorderFactory.createEmptyBorder());
		this.setSize(320, 62);
	}

	public JT(String s) {
		super(s);
		setBorder(BorderFactory.createEmptyBorder());
		this.setSize(320, 62);
	}

	public boolean isOpaque() {
		return false;
	}

}
