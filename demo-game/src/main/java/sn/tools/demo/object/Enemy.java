package sn.tools.demo.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject;
import sn.tools.swing.game.object.GameObject2D;

public class Enemy extends GameObject2D {

	private int x, y;

	public Enemy(int x) {
		this.x = x;
		this.y = 0;
	}

	@Override
	public void update() {
		y += 4;
		if (y > getCanvas().getHeight())
			destroy();
	}

	@Override
	public void onCollision(GameObject other) {
		destroy();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(x, y, 30, 30);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(x, y, 30, 30);
	}
}
