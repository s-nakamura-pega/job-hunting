package sn.tools.demo.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject;
import sn.tools.swing.game.object.GameObject2D;

public class Enemy extends GameObject2D {

	public Enemy(int x) {
		setX(x);
		setY(0);
	}

	@Override
	public void update() {
	}

	@Override
	public void onCollision(GameObject other) {
		destroy();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(getX(), getY(), 30, 30);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(getX(), getY(), 30, 30);
	}

}
