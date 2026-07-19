package sn.tools.swing.game.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

import sn.tools.swing.util.KeyUtils;
import sn.tools.swing.util.KeyUtils.KeyAction;

public abstract class AbstractCanvas extends JComponent {

	private static final long serialVersionUID = 1L;

	private int fps = 60;

	private ScheduledExecutorService loopExecutor;

	private final List<KeyAction> keyActionList = new ArrayList<>();
	private final List<MouseListener> mouseListenerList = new ArrayList<>();
	private final List<MouseMotionListener> mouseMotionListenerList = new ArrayList<>();
	private final List<MouseWheelListener> mouseWheelListenerList = new ArrayList<>();

	public AbstractCanvas() {
		setPreferredSize(monitorSize());
		setMaximumSize(monitorSize());
		setMinimumSize(monitorSize());
	}

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

		// キー
		keyActionList.forEach(ak -> KeyUtils.setKeyAndAction(this, ak));

		// マウス
		mouseListenerList.forEach(this::addMouseListener);
		mouseMotionListenerList.forEach(this::addMouseMotionListener);
		mouseWheelListenerList.forEach(this::addMouseWheelListener);

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

			keyActionList.forEach(ak -> KeyUtils.removeKeyAndAction(this, ak));

			mouseListenerList.forEach(this::removeMouseListener);
			mouseMotionListenerList.forEach(this::removeMouseMotionListener);
			mouseWheelListenerList.forEach(this::removeMouseWheelListener);

			System.out.println("Loop End");
		}
	}

	// ============================
	// KeyAction
	// ============================

	public void addKeyAction(KeyAction action) {
		keyActionList.add(action);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			KeyUtils.setKeyAndAction(this, action);
		}
	}

	public void removeKeyAction(KeyAction action) {
		keyActionList.remove(action);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			KeyUtils.removeKeyAndAction(this, action);
		}
	}

	public void addAllKeyAction(List<KeyAction> actionList) {
		keyActionList.addAll(actionList);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			actionList.forEach(ak -> KeyUtils.setKeyAndAction(this, ak));
		}
	}

	public void removeAllKeyAction(List<KeyAction> actionList) {
		keyActionList.removeAll(actionList);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			actionList.forEach(ak -> KeyUtils.removeKeyAndAction(this, ak));
		}
	}

	// ============================
	// MouseListener
	// ============================

	public void addMouseListenerEx(MouseListener listener) {
		mouseListenerList.add(listener);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			addMouseListener(listener);
		}
	}

	public void removeMouseListenerEx(MouseListener listener) {
		mouseListenerList.remove(listener);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			removeMouseListener(listener);
		}
	}

	public void addAllMouseListener(List<MouseListener> list) {
		mouseListenerList.addAll(list);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			list.forEach(this::addMouseListener);
		}
	}

	public void removeAllMouseListener(List<MouseListener> list) {
		mouseListenerList.removeAll(list);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			list.forEach(this::removeMouseListener);
		}
	}

	// ============================
	// MouseMotionListener
	// ============================

	public void addMouseMotionListenerEx(MouseMotionListener listener) {
		mouseMotionListenerList.add(listener);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			addMouseMotionListener(listener);
		}
	}

	public void removeMouseMotionListenerEx(MouseMotionListener listener) {
		mouseMotionListenerList.remove(listener);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			removeMouseMotionListener(listener);
		}
	}

	public void addAllMouseMotionListener(List<MouseMotionListener> list) {
		mouseMotionListenerList.addAll(list);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			list.forEach(this::addMouseMotionListener);
		}
	}

	public void removeAllMouseMotionListener(List<MouseMotionListener> list) {
		mouseMotionListenerList.removeAll(list);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			list.forEach(this::removeMouseMotionListener);
		}
	}

	// ============================
	// MouseWheelListener
	// ============================

	public void addMouseWheelListenerEx(MouseWheelListener listener) {
		mouseWheelListenerList.add(listener);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			addMouseWheelListener(listener);
		}
	}

	public void removeMouseWheelListenerEx(MouseWheelListener listener) {
		mouseWheelListenerList.remove(listener);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			removeMouseWheelListener(listener);
		}
	}

	public void addAllMouseWheelListener(List<MouseWheelListener> list) {
		mouseWheelListenerList.addAll(list);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			list.forEach(this::addMouseWheelListener);
		}
	}

	public void removeAllMouseWheelListener(List<MouseWheelListener> list) {
		mouseWheelListenerList.removeAll(list);
		if (loopExecutor != null && !loopExecutor.isShutdown()) {
			list.forEach(this::removeMouseWheelListener);
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

	protected abstract Dimension monitorSize();

}
