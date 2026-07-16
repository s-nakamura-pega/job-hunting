package sn.tools.demo.scene;

import sn.tools.demo.object.Player;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.component.GameCanvas;
import sn.tools.swing.game.creator.SceneCreator;

@Scene("game")
public class GameSceneCreator implements SceneCreator {

	private GameCanvas canvas;

	@Override
	public void create() {
		canvas = new GameCanvas();
		canvas.addObject(new Player(b -> canvas.addObject(b)));
	}

	@Override
	public void onEnter(Parameter parameter) {
	}

	@Override
	public void onDisplay(Parameter parameter) {
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
