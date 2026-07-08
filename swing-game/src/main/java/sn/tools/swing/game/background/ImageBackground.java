package sn.tools.swing.game.background;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageBackground implements Background {

	private Image image;
	private int scrollX = 0;
	private int speed = 1;

	public ImageBackground(String path) {
		this.image = new ImageIcon(path).getImage();
	}

	@Override
	public void update() {
		scrollX += speed;
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		g.drawImage(image, -scrollX, 0, width, height, null);
		g.drawImage(image, width - scrollX, 0, width, height, null);

		if (scrollX >= width)
			scrollX = 0;
	}

}
