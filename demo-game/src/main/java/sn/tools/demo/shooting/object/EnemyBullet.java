package sn.tools.demo.shooting.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject;
import sn.tools.swing.game.object.GameObject2D;

public class EnemyBullet extends GameObject2D {

	public EnemyBullet(int x, int y) {
		setX(x);
		setY(y);
	}

	@Override
	public void update() {
		setY(getY() + 10);

		int height = gameCanvasFunction.getCanvasSize().get().height;
		if (getY() > height) {
			destroy();
		}
	}

	@Override
	public void onCollision(GameObject other) {
		destroy();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.PINK);
		g.fillOval(getX(), getY(), 10, 10);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(getX(), getY(), 10, 10);
	}

}
