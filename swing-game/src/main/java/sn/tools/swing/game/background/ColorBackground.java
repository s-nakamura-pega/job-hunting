package sn.tools.swing.game.background;

import java.awt.Color;
import java.awt.Graphics;

public class ColorBackground implements Background {

	private Color color;

	public ColorBackground(Color color) {
		this.color = color;
	}

	@Override
	public void init() {
		// 静的背景なので特に初期化処理は不要
		// ただしライフサイクル整合のため空実装しておく
	}

	@Override
	public void update() {
		// 必要なら色を変えたりアニメしたりできる
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}

	@Override
	public void destroy() {
		// 静的背景なので破棄処理は不要
		// ただし Scene 切り替え時に呼ばれるので空実装でOK
		color = null; // メモリ解放の意味で null にしておくのはアリ
	}

	@Override
	public void onRemove() {
		// 破棄直前の処理（ログやエフェクトなど）
		// 今は何もしないが、ライフサイクル整合のため空実装
	}
}
