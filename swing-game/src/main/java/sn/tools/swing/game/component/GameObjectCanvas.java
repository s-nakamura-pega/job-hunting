package sn.tools.swing.game.component;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import sn.tools.swing.game.background.Background;
import sn.tools.swing.game.object.GameObject;

public class GameObjectCanvas extends AbstractCanvas {

	private static final long serialVersionUID = 1L;

	private Background background;
	private final List<GameObject> objects = new ArrayList<>();

	/** 背景設定（init を呼ぶ） */
	public void setBackground(Background background) {
		if (this.background != null) {
			this.background.destroy();
			this.background.onRemove();
		}
		this.background = background;
		if (this.background != null) {
			this.background.init();
		}
	}

	/** オブジェクト追加（init を呼ぶ） */
	public void addObject(GameObject obj) {
		obj.init();
		objects.add(obj);
	}

	/** ★画面転換用：Canvas を完全クリア */
	public void clear() {

		// オブジェクト破棄
		List<GameObject> snapshot = new ArrayList<>(objects);
		for (GameObject obj : snapshot) {
			obj.destroy();
			obj.onRemove();
		}
		objects.clear();

		// 背景破棄
		if (background != null) {
			background.destroy();
			background.onRemove();
			background = null;
		}
	}

	@Override
	protected void update() {

		// 背景更新
		if (background != null) {
			background.update();
		}

		// オブジェクト更新
		List<GameObject> snapshot = new ArrayList<>(objects);
		for (GameObject obj : snapshot) {
			obj.update();
		}

		// 衝突判定
		checkCollisions();

		// 衝突後処理
		for (GameObject obj : snapshot) {
			obj.postUpdate();
		}

		// destroy フラグのオブジェクトを remove
		removeDestroyedObjects();
	}

	@Override
	protected void draw(Graphics g) {

		// 背景描画
		if (background != null) {
			background.draw(g, getWidth(), getHeight());
		}

		// オブジェクト描画
		List<GameObject> snapshot = new ArrayList<>(objects);
		for (GameObject obj : snapshot) {
			obj.draw(g);
		}
	}

	/** 衝突判定 */
	private void checkCollisions() {
		List<GameObject> snapshot = new ArrayList<>(objects);
		int size = snapshot.size();
		for (int i = 0; i < size; i++) {
			GameObject a = snapshot.get(i);
			Rectangle ra = a.getBounds();

			for (int j = i + 1; j < size; j++) {
				GameObject b = snapshot.get(j);
				Rectangle rb = b.getBounds();

				if (ra.intersects(rb)) {
					a.onCollision(b);
					b.onCollision(a);
				}
			}
		}
	}

	/** destroy フラグのオブジェクトを安全に削除 */
	private void removeDestroyedObjects() {
		objects.removeIf(obj -> {
			if (obj.isDestroyed()) {
				obj.onRemove();
				return true;
			}
			return false;
		});
	}
}
