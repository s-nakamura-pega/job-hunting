package sn.tools.swing.game.object;

import java.awt.Graphics;

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

}
