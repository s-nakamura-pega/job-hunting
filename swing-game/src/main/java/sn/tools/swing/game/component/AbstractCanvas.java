package sn.tools.swing.game.component;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

public abstract class AbstractCanvas extends JComponent {

	private static final long serialVersionUID = 1L;

	private int fps = 60;

	private ScheduledExecutorService loopExecutor;

	public void setFps(int fps) {
		this.fps = fps;
	}

	public int getFps() {
		return fps;
	}

	public void startLoop() {
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			return;
		}
		System.out.println("Loop Start");
		loopExecutor = Executors.newSingleThreadScheduledExecutor();
		long frameIntervalNs = 1_000_000_000L / fps;
		loopExecutor.scheduleAtFixedRate(() -> {
			update();
			repaint();
		}, 0, frameIntervalNs, TimeUnit.NANOSECONDS);
	}

	public void stopLoop() {
		if (loopExecutor != null) {
			loopExecutor.shutdownNow();
			loopExecutor = null;
			System.out.println("Loop End");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		Toolkit.getDefaultToolkit().sync();
	}

	protected abstract void update();

	protected abstract void draw(Graphics g);
}
