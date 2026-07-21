package sn.tools.demo.screen;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import sn.tools.demo.shooting.canvas.TitleCanvas;
import sn.tools.demo.shooting.panel.ShootingGamePanel;
import sn.tools.swing.flow.annotation.Screen;

import sn.tools.swing.flow.expansion.screen.GamePanelScreenCreator;
import sn.tools.swing.game.panel.GamePanel;
import sn.tools.swing.util.ComponentUtils;
import sn.tools.swing.util.WindowUtils;

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

	@Override
	public ImageIcon getScreenIcon() {
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());
		TitleCanvas title = new TitleCanvas();
		title.setSize(ShootingGamePanel.MONITOR_SIZE);
		menuPanel.add(title);
		Dimension size = WindowUtils.getScreenRatioSize(0.7);
		BufferedImage img = ComponentUtils.panelToImage(menuPanel, size);
		return new ImageIcon(img);
	}

}
