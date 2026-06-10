package sn.tools.swing.flow.frame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sn.tools.swing.flow.controller.ScreenController;
import sn.tools.swing.util.WindowUtils;

public abstract class FlowScreenFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final ScreenController controller;

	public FlowScreenFrame() {
		super();
		setSize(WindowUtils.getScreenRatioSize(0.5));
		controller = new ScreenController(scanPackage());
		setContentPane(controller.getScreen(initScreenId()));
		onInit();
	}

	public void flowScreen(String screenId) {
		SwingUtilities.invokeLater(() -> {
			setContentPane(controller.getScreen(screenId));
			revalidate();
			repaint();
		});
	}

	protected abstract String scanPackage();

	protected abstract String initScreenId();

	protected abstract void onInit();

}
