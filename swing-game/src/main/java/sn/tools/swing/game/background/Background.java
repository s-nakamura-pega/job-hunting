package sn.tools.swing.game.background;

import java.awt.Graphics;

public interface Background {

	void init();

	void update();

	void draw(Graphics g, int width, int height);

	void destroy();

	void onRemove();

}
