package sn.tools.swing.game.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import sn.tools.swing.game.background.Background;
import sn.tools.swing.game.object.GameObject;

public class GameCanvas extends AbstractCanvas {

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
		obj.setGameCanvasFunction(new GameCanvasFunction(gb -> addObject(gb),
				p -> objects.stream().filter(p::test).toList(), () -> new Dimension(getWidth(), getHeight())));
		obj.init();
		objects.add(obj);
		addAllKeyAction(obj.getKeyActionList());
		addAllMouseListener(obj.getMouseListenerList());
		addAllMouseMotionListener(obj.getMouseMotionListenerList());
		addAllMouseWheelListener(obj.getMouseWheelListenerList());
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

		// ★画面外オブジェクトを destroy
		removeOutOfBoundsObjects();

		// destroy フラグのオブジェクトを remove
		removeDestroyedObjects();
	}

	@Override
	protected void draw(Graphics g) {

		// 背景描画
		if (background != null) {
			background.draw(g, getWidth(), getHeight());
		}

		// オブジェクト描画（2D/3D 両対応）
		List<GameObject> snapshot = new ArrayList<>(objects);
		for (GameObject obj : snapshot) {
			obj.draw(g); // 3D オブジェクトはここで JNI を呼ぶ
		}
	}

	/** 衝突判定（intersects に一本化） */
	private void checkCollisions() {
		List<GameObject> snapshot = new ArrayList<>(objects);
		int size = snapshot.size();
		for (int i = 0; i < size; i++) {
			GameObject a = snapshot.get(i);

			for (int j = i + 1; j < size; j++) {
				GameObject b = snapshot.get(j);

				if (a.intersects(b)) {
					a.onCollision(b);
					b.onCollision(a);
				}
			}
		}
	}

	/** 画面外に出たオブジェクトを destroy する */
	private void removeOutOfBoundsObjects() {
		int w = getWidth();
		int h = getHeight();
		for (GameObject obj : objects) {
			int x = obj.getX();
			int y = obj.getY();
			int ow = obj.getWidth();
			int oh = obj.getHeight();
			// 完全に画面外に出たら destroy
			if (x + ow < 0 || x > w || y + oh < 0 || y > h) {
				obj.destroy();
			}
		}
	}

	/** destroy フラグのオブジェクトを安全に削除 */
	private void removeDestroyedObjects() {
		objects.removeIf(obj -> {
			if (obj.isDestroyed()) {
				obj.onRemove();
				removeAllKeyAction(obj.getKeyActionList());
				removeAllMouseListener(obj.getMouseListenerList());
				removeAllMouseMotionListener(obj.getMouseMotionListenerList());
				removeAllMouseWheelListener(obj.getMouseWheelListenerList());
				return true;
			}
			return false;
		});
	}

	public static record GameCanvasFunction(Consumer<GameObject> addObject,
			Function<Predicate<GameObject>, List<GameObject>> getObjects, Supplier<Dimension> getCanvasSize) {
	}

}
