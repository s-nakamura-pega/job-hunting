package sn.tools.swing.game.background;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageBackground implements Background {

	private Image image;
	private int scrollX = 0;
	private int speed = 1;
	private String path;

	public ImageBackground(String path) {
		this.path = path;
	}

	@Override
	public void init() {
		// 画像読み込みは init で行うのが正しいライフサイクル
		image = new ImageIcon(path).getImage();
		scrollX = 0;
	}

	@Override
	public void update() {
		scrollX += speed;
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		if (image == null) {
			return;
		}

		// スクロール描画（ループ背景）
		g.drawImage(image, -scrollX, 0, width, height, null);
		g.drawImage(image, width - scrollX, 0, width, height, null);

		if (scrollX >= width) {
			scrollX = 0;
		}
	}

	@Override
	public void destroy() {
		// 画像破棄（GC に任せるため null にする）
		image = null;
	}

	@Override
	public void onRemove() {
		// 背景破棄直前の処理（ログやエフェクトなど）
		// 今は空で OK
	}

}
