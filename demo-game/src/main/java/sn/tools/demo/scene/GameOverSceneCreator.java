package sn.tools.demo.scene;

import sn.tools.demo.canvas.GameOverCanvas;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.creator.SceneCreator;

@Scene("gameover")
public class GameOverSceneCreator implements SceneCreator {

	private GameOverCanvas canvas;

	@Override
	public void create() {
		canvas = new GameOverCanvas();
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
	}

	@Override
	public void reload() {
	}

	@Override
	public AbstractCanvas getCreation() {
		return canvas;
	}
}
