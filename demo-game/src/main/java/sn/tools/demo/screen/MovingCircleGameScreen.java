package sn.tools.demo.screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.expansion.screen.CanvasScreenCreator;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.game.background.ColorBackground;
import sn.tools.swing.game.component.GameCanvas;
import sn.tools.swing.game.object.GameObject;
import sn.tools.swing.game.object.GameObject2D;

@Screen("moving-Circle")
public class MovingCircleGameScreen extends CanvasScreenCreator<GameCanvas> {

	private GameCanvas canvas = new GameCanvas();

	private ScheduledExecutorService loopExecutor;

	@Override
	protected int fps() {
		return 60;
	}

	@Override
	protected GameCanvas canvas() {
		return canvas;
	}

	@Override
	protected void OnInit() {
		canvas.setBackground(new ColorBackground(new Color(30, 30, 60)));

	}

	@Override
	public void onDisplay(ScreenParameter parameter) {
		super.onDisplay(parameter);
		loopExecutor = Executors.newSingleThreadScheduledExecutor();
		loopExecutor.scheduleAtFixedRate(() -> {
			if (canvas.isShowing()) {
				MovingCircle circle = new MovingCircle();
				canvas.addObject(circle);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	@Override
	public void onExit() {
		super.onExit();
		loopExecutor.shutdownNow();
		loopExecutor = null;
	}

	public class MovingCircle extends GameObject2D {

		private int x = 50;
		private int y = 300;
		private int vx = 4;
		private final int r = 40;

		@Override
		public void init() {
			// 初期化処理が必要ならここに書く
		}

		@Override
		public void update() {
			x += vx;

			// 左右反転
			if (x - r < 0 || x + r > SwingUtilities.getWindowAncestor(canvas).getWidth()) {
				vx = -vx;
			}
		}

		@Override
		public void postUpdate() {
			// 衝突後の処理が必要ならここに書く
		}

		@Override
		public void onCollision(GameObject other) {
			destroy();
			other.destroy();
		}

		@Override
		public void onRemove() {
			// 破棄直前の処理（ログやエフェクトなど）
		}

		@Override
		public void draw(Graphics g) {
			g.setColor(new Color(240, 240, 80));
			g.fillOval(x - r, y - r, r * 2, r * 2);
		}

		@Override
		protected Rectangle getRect() {
			return new Rectangle(x - r, y - r, r * 2, r * 2);
		}

	}

}
