package su.nextgen.dev.asn007.nloader.gui;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;

public class JP extends JPasswordField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8494304992205463759L;

	public JP() {
		super();
		setBorder(BorderFactory.createEmptyBorder());
		this.setSize(320, 62);
	}

	public JP(String s) {
		super(s);
		setBorder(BorderFactory.createEmptyBorder());
		this.setSize(320, 62);
	}

	public boolean isOpaque() {
		return false;
	}

}
