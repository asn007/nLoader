package su.nextgen.dev.asn007.nloader.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Button extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1540968877485846865L;
	private String text = "ButtonText";
	private Color btnColor = Color.gray;
	private Font btnFont;
	private Color textColor = Color.black;
	private Color hoverColor;
	private boolean hover = false;
	public Button(String text, Color btnColor, Font btnFont, Color textColor, Color hoverColor) {
		this.text = text;
		this.btnColor = btnColor;
		this.btnFont = btnFont;
		this.textColor = textColor;
		this.hoverColor = hoverColor;
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setHover(true);
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setHover(false);
				
			}});
	}
	
	public Color getForeground() {
		return this.textColor;
	}
	
	public Color getBackground() {
		return this.btnColor;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Font getFont() {
		return this.btnFont;
	}
	
	public Color getBackgroundHover() {
		return this.hoverColor;
	}
	
	public void setHoverBackground(Color c)	{
		this.hoverColor = c;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setForeground(Color c)	{
		this.textColor = c;
	}
	
	public void setBackground(Color c)	{
		this.btnColor = c;
	}
	
	public boolean isHover()  {
		return hover;
	}
	
	public void setHover(boolean b)  {
		this.hover = b;
	}
	
	public void paintComponent(Graphics g)	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			      RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(getFont());
		g2d.clearRect(getX(), getY(), getWidth(), getHeight());
		if(!isHover()) 
			g2d.setColor(getBackground());
		else g2d.setColor(getBackgroundHover());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(getForeground());
		g2d.drawString(getText(), getWidth() / 2 - (g2d.getFontMetrics().stringWidth(getText()) / 2), getHeight() / 2 + g2d.getFontMetrics().getDescent());
	}

}
