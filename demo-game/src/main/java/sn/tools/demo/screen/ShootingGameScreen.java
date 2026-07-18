package sn.tools.demo.screen;

import sn.tools.demo.shooting.panel.ShootingGamePanel;
import sn.tools.swing.flow.annotation.Screen;

import sn.tools.swing.flow.expansion.screen.GamePanelScreenCreator;
import sn.tools.swing.game.panel.GamePanel;

@Screen("shooting")
public class ShootingGameScreen extends GamePanelScreenCreator {

	private final GamePanel panel = new ShootingGamePanel();

	@Override
	protected GamePanel gamePanel() {
		return panel;
	}

	@Override
	protected void onInit() {
	}

}
