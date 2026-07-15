package sn.tools.swing.flow.screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sn.tools.swing.flow.controller.ScreenController.ScreenCatalog;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.util.ComponentUtils;
import sn.tools.swing.util.WindowUtils;

public class MenuScreen extends JPanel {

	private static final long serialVersionUID = 1L;

	protected List<ScreenCatalog> screenCatalogList;

	public MenuScreen(List<ScreenCatalog> screenCatalogList) {
		this.screenCatalogList = screenCatalogList;
		create();
	}

	protected void create() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		screenCatalogList.forEach(sc -> {
			Dimension size = WindowUtils.getScreenRatioSize(0.40);
			JButton btn = new JButton(
					ComponentUtils.createScaledIcon(sc.screenIcon().getImage(), size.width, size.height));
			btn.setToolTipText(sc.screenName());
			btn.setPreferredSize(size);
			btn.addActionListener(e -> {
				FlowScreenFrame.flow(e, sc.id(), new SimpleParameter());
			});
			panel.add(btn);
		});
		add(new JScrollPane(panel));
	}

}
