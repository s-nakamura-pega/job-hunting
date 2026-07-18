package sn.tools.demo.shooting.panel;

import sn.tools.swing.game.panel.GamePanel;

public class ShootingGamePanel extends GamePanel {

	private static final long serialVersionUID = 1L;

	@Override
	protected String scanScenePackage() {
		return "sn.tools.demo.shooting.scene";
	}

	@Override
	protected String initSceneId() {
		return "title";
	}

	@Override
	protected void onInit() {
	}

}
