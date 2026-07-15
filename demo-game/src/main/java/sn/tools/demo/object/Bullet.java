package sn.tools.demo.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject2D;

public class Bullet extends GameObject2D {

	private int x, y;

	public Bullet(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		y -= 10;
		if (y < 0)
			destroy();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, 10, 10);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(x, y, 10, 10);
	}
}
