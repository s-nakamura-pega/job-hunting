package sn.tools.swing.flow.frame;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sn.tools.swing.flow.context.MenuBarContext;
import sn.tools.swing.flow.context.ScreenContext;
import sn.tools.swing.flow.controller.MenuBarController;
import sn.tools.swing.flow.controller.ScreenController;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.flow.screen.MenuScreen;
import sn.tools.swing.util.WindowUtils;

public abstract class FlowScreenFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final MenuBarController menuController;
	private final ScreenController screenController;

	public FlowScreenFrame() {
		super();
		setSize(WindowUtils.getScreenRatioSize(0.7));
		setLocationRelativeTo(null);
		String scanMemuBarPackage = scanMenuBarPackage();
		menuController = (scanMemuBarPackage == null) ? null : new MenuBarController(scanMenuBarPackage());
		flowMenuBar(initMenuBarId(), new SimpleScreenParameter());
		screenController = new ScreenController(scanScreenPackage(), getMenuScreenClass());
		flowScreen(initScreenId(), new SimpleScreenParameter());
		onInit();
	}

	public void flowMenuBar(String screenId, ScreenParameter parameter) {
		menuController.flow(screenId, new MenuBarContext(this, parameter));
	}

	public void flowMenuBar(JMenuBar menuBar) {
		SwingUtilities.invokeLater(() -> {
			setJMenuBar(menuBar);
			revalidate();
			repaint();
		});
	}

	public void flowScreen(String screenId, ScreenParameter parameter) {
		screenController.flow(screenId, new ScreenContext(this, parameter));
	}

	public void flowScreen(JPanel panel) {
		SwingUtilities.invokeLater(() -> {
			setContentPane(panel);
			revalidate();
			repaint();
		});
	}

	public void flowMenuScreen() {
		screenController.flowMenuScreen(this);
	}

	protected Class<? extends MenuScreen> getMenuScreenClass() {
		return MenuScreen.class;
	}

	protected abstract String scanMenuBarPackage();

	protected abstract String initMenuBarId();

	protected abstract String scanScreenPackage();

	protected abstract String initScreenId();

	protected abstract void onInit();

	public static void flow(ActionEvent event, String screenId, ScreenParameter parameter) {
		FlowScreenFrame frame = WindowUtils.getWindow(event, FlowScreenFrame.class);
		if (frame != null) {
			frame.flowScreen(screenId, parameter);
		}
	}

	public static void flow(ActionEvent event, String screenId, ScreenParameter screenParam, String menuBarId,
			ScreenParameter menuBarParam) {
		FlowScreenFrame frame = WindowUtils.getWindow(event, FlowScreenFrame.class);
		if (frame != null) {
			frame.flowMenuBar(menuBarId, menuBarParam);
			frame.flowScreen(screenId, screenParam);
		}
	}

	public static void flowMenu(ActionEvent event) {
		FlowScreenFrame frame = WindowUtils.getWindow(event, FlowScreenFrame.class);
		if (frame != null) {
			frame.flowMenuScreen();
		}
	}

}
