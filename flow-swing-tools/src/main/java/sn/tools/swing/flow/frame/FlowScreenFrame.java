package sn.tools.swing.flow.frame;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sn.tools.swing.flow.controller.ScreenController;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.util.WindowUtils;

public abstract class FlowScreenFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final ScreenController controller;

	public FlowScreenFrame() {
		super();
		setSize(WindowUtils.getScreenRatioSize(0.7));
		setLocationRelativeTo(null);
		controller = new ScreenController(scanPackage());
		setContentPane(controller.getScreen(initScreenId(), new SimpleScreenParameter()));
		onInit();
	}

	public void flowScreen(String screenId, ScreenParameter parameter) {
		SwingUtilities.invokeLater(() -> {
			setContentPane(controller.getScreen(screenId, parameter));
			revalidate();
			repaint();
		});
	}

	protected abstract String scanPackage();

	protected abstract String initScreenId();

	protected abstract void onInit();

	public static void flow(ActionEvent event, String screenId, ScreenParameter parameter) {
		FlowScreenFrame frame = WindowUtils.getWindow(event, FlowScreenFrame.class);
		if (frame != null) {
			frame.flowScreen(screenId, parameter);
		}
	}

}
