package sn.tools.demo.shooting.object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.game.object.GameObject;
import sn.tools.swing.game.object.GameObject2D;

public class Enemy extends GameObject2D {

	private int dx = 2; // 横移動速度
	private long lastShot = 0; // 最後に弾を撃った時間
	private final long shotInterval = 800; // 800msごとに攻撃

	@Override
	public void init() {
		Dimension size = gameCanvasFunction.getCanvasSize().get();
		setX((int) (Math.random() * (size.width - 30))); // ランダム位置
		setY(10); // 上から出現
	}

	@Override
	public void update() {
		// --- 横移動のみ ---
		setX(getX() + dx);

		// 画面端で反転
		Dimension size = gameCanvasFunction.getCanvasSize().get();
		if (getX() < 0 || getX() > size.width - 30) {
			dx = -dx;
		}

		// --- 攻撃（自動で弾を撃つ） ---
		long now = System.currentTimeMillis();
		if (now - lastShot > shotInterval) {
			gameCanvasFunction.addObject().accept(new EnemyBullet(getX() + 10, getY() + 30));
			lastShot = now;
		}
	}

	@Override
	public void onCollision(GameObject other) {
		if (other instanceof Bullet) {
			destroy();
			gameCanvasFunction.flow().accept("gameclear", new SimpleParameter());
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(getX(), getY(), 30, 25);
		g.fillRect(getX() + 13, getY() + 25, 4, 5);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(getX(), getY(), 30, 30);
	}
}
