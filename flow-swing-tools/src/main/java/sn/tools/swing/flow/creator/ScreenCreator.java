package sn.tools.swing.flow.creator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import sn.tools.swing.flow.controller.ScreenController.ScreenCatalog;
import sn.tools.swing.util.ComponentUtils;
import sn.tools.swing.util.WindowUtils;

public interface ScreenCreator extends Creator<JPanel> {

	default String getScreenName() {
		return getClass().getSimpleName();
	}

	default ImageIcon getScreenIcon() {
		JPanel panel = getCreation();
		Dimension size = WindowUtils.getScreenRatioSize(0.7);
		BufferedImage img = ComponentUtils.panelToImage(panel, size);
		return new ImageIcon(img);
	}

	default boolean isDisplayCatalog() {
		return true;
	}

}
