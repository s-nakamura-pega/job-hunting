package sn.tools.swing.game.component;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;

public abstract class AbstractCanvas extends JComponent {

	private static final long serialVersionUID = 1L;

	private int fps = 60;
	private Timer loopTimer;

	public void setFps(int fps) {
		this.fps = fps;
	}

	public int getFps() {
		return fps;
	}

	public void startLoop() {
		if (loopTimer == null) {
			loopTimer = new Timer(1000 / fps, _ -> {
				update();
				repaint();
			});
			loopTimer.start();
		} else {
			loopTimer.restart();
		}
	}

	public void stopLoop() {
		if (loopTimer != null) {
			loopTimer.stop();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	protected abstract void update();

	protected abstract void draw(Graphics g);
}
