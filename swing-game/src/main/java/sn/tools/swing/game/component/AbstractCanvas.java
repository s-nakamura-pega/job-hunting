package sn.tools.swing.game.component;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;

import sn.tools.swing.game.background.Background;

public abstract class AbstractCanvas extends JComponent {

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private int fps;
	private Background background;

	private void initTimer() {
		if (timer != null) {
			stopLoop();
		}
		int delay = 1000 / fps;
		timer = new Timer(delay, _ -> gameLoop());
	}

	private void gameLoop() {
		if (background != null) {
			background.update();
		}
		update();
		repaint();
	}

	public void startLoop() {
		if (!timer.isRunning())
			timer.start();
	}

	public void stopLoop() {
		if (timer.isRunning())
			timer.stop();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (background != null) {
			background.draw(g, getWidth(), getHeight());
		}

		draw(g);
	}

	public void setFps(int fps) {
		this.fps = fps;
		initTimer();
	};

	public void setBackground(Background background) {
		this.background = background;
	};

	protected abstract void update();

	protected abstract void draw(Graphics g);

}
