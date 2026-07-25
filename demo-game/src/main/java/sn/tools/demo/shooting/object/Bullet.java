package sn.tools.demo.shooting.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject2D;

public class Bullet extends GameObject2D {

	public Bullet(int x, int y) {
		setX(x);
		setY(y);
	}

	@Override
	public void update() {
		setY(getY() - 10);
		if (getY() < 0)
			destroy();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(getX(), getY(), 10, 10);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(getX(), getY(), 10, 10);
	}

}
