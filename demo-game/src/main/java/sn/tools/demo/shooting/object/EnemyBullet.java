package sn.tools.demo.shooting.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject;
import sn.tools.swing.game.object.GameObject2D;

public class EnemyBullet extends GameObject2D {

	private int x, y;
	private final int dy = 8; // 敵弾の速度

	public EnemyBullet(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		y += dy;

		int height = gameCanvasFunction.getCanvasSize().get().height;
		if (y > height) {
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
		g.fillOval(x, y, 10, 10);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(x, y, 10, 10);
	}
}
