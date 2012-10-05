package su.nextgen.dev.asn007.nloader.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JProgressBar;

public class ProgressBar extends JProgressBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1633549614091038080L;
	private int value;
	//private int maximum = 100;
	
	
	public ProgressBar()
	{
		
	}

	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
		repaint();
	}
	
//	public int getMax()
//	{
//		return maximum;
//	}
//	
//	public void setMax(int maximum)
//	{
//		this.maximum = maximum;
//	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.decode("#ff5a5a"));
        g2d.fillRect(0, 0, (int)(value * getWidth() / getMaximum()), getHeight());
        
	}
	
}
