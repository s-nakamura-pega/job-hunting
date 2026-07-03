package sn.tools.swing.flow.creator;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import sn.tools.swing.util.ComponentUtils;

public interface ScreenCreator extends Creator<JPanel> {

	default String getScreenName() {
		return getClass().getSimpleName();
	}

	default Icon getScreenIcon() {
		JPanel panel = getCreation();
		BufferedImage img = ComponentUtils.panelToImage(panel);
		return new ImageIcon(img);
	}

	default boolean isDisplayCatalog() {
		return true;
	}

}
