package sn.tools.swing.flow.expansion.screen;

import javax.swing.JPanel;

import sn.tools.swing.flow.creator.ScreenCreator;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.panel.GamePanel;

public abstract class GamePanelScreenCreator implements ScreenCreator {

	private GamePanel panel;

	@Override
	public void create() {
		this.panel = gamePanel();
		onInit();
	}

	@Override
	public void onEnter(Parameter parameter) {
	}

	@Override
	public void onDisplay(Parameter parameter) {
		panel.startGame();
	}

	@Override
	public void onExit() {
		panel.stopGame();
	}

	@Override
	public void reload() {
	}

	@Override
	public JPanel getCreation() {
		return panel;
	}

	protected abstract GamePanel gamePanel();

	protected abstract void onInit();

}
