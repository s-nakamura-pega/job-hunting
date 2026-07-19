package sn.tools.demo.shooting.scene;

import java.awt.Color;
import java.awt.Dimension;

import sn.tools.demo.shooting.object.Player;
import sn.tools.demo.shooting.panel.ShootingGamePanel;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.background.ColorBackground;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.component.GameCanvas;
import sn.tools.swing.game.creator.SceneCreator;

@Scene("game")
public class GameSceneCreator implements SceneCreator {

	private GameCanvas canvas;

	@Override
	public void create() {
		canvas = new GameCanvas() {

			@Override
			protected Dimension monitorSize() {
				return ShootingGamePanel.MONITOR_SIZE;
			}

		};
		canvas.setBackground(new ColorBackground(new Color(30, 30, 60)));
	}

	@Override
	public void onEnter(Parameter parameter) {
	}

	@Override
	public void onDisplay(Parameter parameter) {
		canvas.addObject(new Player());
		canvas.startLoop();
	}

	@Override
	public void onExit() {
		canvas.stopLoop();
		canvas.clear();
	}

	@Override
	public void reload() {
	}

	@Override
	public AbstractCanvas getCreation() {
		return canvas;
	}
}
