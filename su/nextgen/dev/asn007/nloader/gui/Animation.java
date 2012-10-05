package su.nextgen.dev.asn007.nloader.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Animation implements ActionListener {

	public Component oldComp;
	public Component newComp;
	public Timer timer;
	public int oldX;
	public int newX;
	public int goodX;
	public JPanel parentComp;
	public AnimationType anitype;
	public static boolean animationRunning = false;

	public Animation(Component oldComp, Component newComp, int goodX,
			JPanel parentComp, AnimationType type) {
		this.oldComp = oldComp;
		this.newComp = newComp;
		this.goodX = goodX;
		timer = new Timer(1, this);
		this.anitype = type;
		this.parentComp = parentComp;
	}

	@SuppressWarnings("static-access")
	public void start() {
		while (animationRunning) {
			try {
				Thread.currentThread().sleep(100L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		animationRunning = true;
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		oldX = oldComp.getX();
		newX = newComp.getX();
		if (anitype == AnimationType.RIGHT_TO_LEFT_SLIDE) {
			oldX--;
			newX--;
		} else {
			oldX++;
			newX++;
		}
		if (newX != goodX) {
			oldComp.setBounds(oldX, oldComp.getY(), oldComp.getWidth(),
					oldComp.getHeight());
			newComp.setBounds(newX, newComp.getY(), newComp.getWidth(),
					newComp.getHeight());
			// newComp.repaint();
			// oldComp.repaint();
			// parentComp.repaint();
		} else {
			// parentComp.remove(oldComp); No remove for now
			// newComp.repaint();
			// oldComp.repaint();
			// parentComp.repaint();
			animationRunning = false;
			timer.stop();
		}

	}

}
