package sn.tools.demo.screen;

import java.awt.Color;
import java.awt.Graphics;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.expansion.screen.GameScreenCreator;
import sn.tools.swing.game.background.ColorBackground;
import sn.tools.swing.game.component.GameObjectCanvas;
import sn.tools.swing.game.object.GameObject;

@Screen("moving-Circle")
public class MovingCircleGameScreen extends GameScreenCreator<GameObjectCanvas> {

	private GameObjectCanvas canvas = new GameObjectCanvas();

	@Override
	protected int fps() {
		return 60;
	}

	@Override
	protected GameObjectCanvas canvas() {
		return canvas;
	}

	@Override
	protected void OnInit() {
		canvas.setBackground(new ColorBackground(new Color(30, 30, 60)));
		MovingCircle circle = new MovingCircle();
		circle.setCanvasWidth(800);
		canvas.addObject(circle);
	}

	public class MovingCircle implements GameObject {

		private int x = 50;
		private int y = 300;
		private int vx = 4;
		private final int r = 40;

		private int canvasWidth = 800;

		public void setCanvasWidth(int width) {
			this.canvasWidth = width;
		}

		@Override
		public void update() {
			x += vx;

			if (x - r < 0 || x + r > canvasWidth) {
				vx = -vx;
			}
		}

		@Override
		public void draw(Graphics g) {
			g.setColor(new Color(240, 240, 80));
			g.fillOval(x - r, y - r, r * 2, r * 2);
		}
	}

}
