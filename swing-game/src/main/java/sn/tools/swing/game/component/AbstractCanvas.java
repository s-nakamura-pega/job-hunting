package sn.tools.swing.game.component;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public abstract class AbstractCanvas extends JComponent {

	private static final long serialVersionUID = 1L;

	private int fps = 60;
	private Timer loopTimer;

	public void setFps(int fps) {
		this.fps = fps;

		// ★ Timer の生成は必ず EDT で行う
		SwingUtilities.invokeLater(() -> {
			loopTimer = new Timer(1000 / fps, _ -> {
				update();
				repaint();
			});
		});
	}

	public int getFps() {
		return fps;
	}

	public void startLoop() {
		// ★ Timer がまだ生成されていない可能性があるので EDT で保証
		SwingUtilities.invokeLater(() -> {
			if (loopTimer == null) {
				loopTimer = new Timer(1000 / fps, _ -> {
					update();
					repaint();
				});
			}
			loopTimer.start();
		});
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
