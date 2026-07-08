package sn.tools.swing.game.background;

import java.awt.Color;
import java.awt.Graphics;

public class ColorBackground implements Background {

	private Color color;

	public ColorBackground(Color color) {
		this.color = color;
	}

	@Override
	public void update() {
		// 必要なら色を変えたりアニメしたりできる
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}

}
