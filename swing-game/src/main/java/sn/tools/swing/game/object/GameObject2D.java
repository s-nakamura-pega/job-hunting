package sn.tools.swing.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject2D implements GameObject {

	protected boolean destroyed = false;

	private int x;
	private int y;
	private int width;
	private int height;

	/** 子クラスが必ず矩形を返す */
	protected abstract Rectangle getRect();

	/** 2D 衝突判定（子クラスの getRect を使う） */
	@Override
	public boolean intersects(GameObject other) {
		if (other instanceof GameObject2D o2) {
			return this.getRect().intersects(o2.getRect());
		}
		return false; // 3D とは衝突しない仕様
	}

	/** 2D 描画（子クラスが実装） */
	@Override
	public abstract void draw(Graphics g);

	/** ライフサイクル */
	@Override
	public void destroy() {
		destroyed = true;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void onRemove() {
		// 必要なら子クラスで override
	}

	@Override
	public void init() {
		// 必要なら子クラスで override
	}

	@Override
	public void update() {
		// 必要なら子クラスで override
	}

	@Override
	public void postUpdate() {
		// 必要なら子クラスで override
	}

	@Override
	public void onCollision(GameObject other) {
		// 必要なら子クラスで override
	}

	@Override
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
