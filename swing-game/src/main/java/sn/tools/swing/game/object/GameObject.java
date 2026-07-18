package sn.tools.swing.game.object;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.List;

import sn.tools.swing.game.component.GameCanvas.GameCanvasFunction;
import sn.tools.swing.util.KeyUtils.KeyAction;

public interface GameObject {

	void init();

	void update();

	void postUpdate();

	public void onCollision(GameObject other);

	void onRemove();

	boolean isDestroyed();

	void destroy();

	void draw(Graphics g);

	boolean intersects(GameObject other);

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	default List<KeyAction> getKeyActionList() {
		return List.of();
	}

	default List<MouseListener> getMouseListenerList() {
		return List.of();
	}

	default List<MouseMotionListener> getMouseMotionListenerList() {
		return List.of();
	}

	default List<MouseWheelListener> getMouseWheelListenerList() {
		return List.of();
	}
	
	void setGameCanvasFunction(GameCanvasFunction gameCanvasFunction);
	
}
