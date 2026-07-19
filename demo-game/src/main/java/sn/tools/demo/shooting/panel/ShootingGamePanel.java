package sn.tools.demo.shooting.panel;

import java.awt.Dimension;

import sn.tools.swing.game.panel.GamePanel;

public class ShootingGamePanel extends GamePanel {

	private static final long serialVersionUID = 1L;
	public static final Dimension MONITOR_SIZE = new Dimension(550, 450);

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
