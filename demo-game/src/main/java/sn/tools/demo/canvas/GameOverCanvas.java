package sn.tools.demo.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import sn.tools.swing.game.component.AbstractCanvas;

public class GameOverCanvas extends AbstractCanvas {

	private static final long serialVersionUID = 1L;

	private boolean clicked = false;

	@Override
	protected void update() {
		if (getMousePressed()) {
			clicked = true;
		}
	}

	@Override
	protected void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("GAME OVER", 120, 200);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 32));
		g.drawString("CLICK TO TITLE", 150, 350);
	}

	@Override
	protected void afterUpdate() {
		if (clicked) {
			getPanel().flowScene("title");
		}
	}
}
