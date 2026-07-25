package sn.tools.demo.shooting.scene;

import java.awt.Color;
import java.awt.Dimension;

import sn.tools.demo.shooting.object.Enemy;
import sn.tools.demo.shooting.object.Player;
import sn.tools.demo.shooting.panel.ShootingGamePanel;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.background.ColorBackground;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.component.GameCanvas;
import sn.tools.swing.game.creator.SceneCreator;

@Scene("game")
public class GameSceneCreator extends SceneCreator {

	private GameCanvas canvas;

	@Override
	public void create() {
		canvas = new GameCanvas() {

			@Override
			protected Dimension monitorSize() {
				return ShootingGamePanel.MONITOR_SIZE;
			}

		};
	}

	@Override
	public AbstractCanvas getCreation() {
		return canvas;
	}

	@Override
	protected void init(Parameter parameter) {
		canvas.setBackground(new ColorBackground(new Color(30, 30, 60)));
		canvas.addObject(new Player());
		canvas.addObject(new Enemy());
		canvas.addObject(new Enemy());
	}

	@Override
	protected void cleanup() {
	}

}
