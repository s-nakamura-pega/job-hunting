package sn.tools.swing.flow.frame;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.swing.flow.context.MenuBarContext;
import sn.tools.swing.flow.context.ScreenContext;
import sn.tools.swing.flow.controller.MenuBarController;
import sn.tools.swing.flow.controller.ScreenController;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.flow.screen.MenuScreen;
import sn.tools.swing.flow.screen.TitleScreen;
import sn.tools.swing.util.WindowUtils;

public abstract class FlowScreenFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final long WAIT_MIN_TIME = 3000;

	private MenuBarController menuController;
	private ScreenController screenController;

	public FlowScreenFrame() {
		super();
		Date start = new Date();
		setSize(WindowUtils.getScreenRatioSize(0.7));
		setLocationRelativeTo(null);
		TitleScreen titleScreen = new TitleScreen();
		setContentPane(titleScreen);
		titleScreen.startProcessingTimer();
		SwingWorker<Void, String> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws Exception {
				String pkg = scanMenuBarPackage();
				menuController = (pkg == null || pkg.isBlank()) ? null : new MenuBarController(pkg);
				screenController = new ScreenController(scanScreenPackage(), getMenuScreenClass());
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
			}

			@Override
			protected void done() {
				Date end = new Date();
				long procTime = end.getTime() - start.getTime();
				Timer timer = new Timer((int) (procTime < WAIT_MIN_TIME ? WAIT_MIN_TIME - procTime : 0), _ -> {
					try {
						titleScreen.stopProcessingTimer();
						get();
						String menuId = initMenuBarId();
						if (menuId != null && !menuId.isBlank()) {
							flowMenuBar(menuId, new SimpleScreenParameter());
						}
						String screenId = initScreenId();
						if (screenId != null && !screenId.isBlank()) {
							flowScreen(screenId, new SimpleScreenParameter());
						} else {
							flowMenuScreen();
						}
						onInit();
					} catch (Exception ex) {
						ex.printStackTrace();
						Exception root = ExceptionUtils.getRootCause(ex);
						JOptionPane.showMessageDialog(FlowScreenFrame.this, root.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
						FlowScreenFrame.this.dispose();
					}
				});
				timer.setRepeats(false);
				timer.start();
			}

		};
		worker.execute();
	}

	public void flowMenuBar(String screenId, ScreenParameter parameter) {
		if (menuController == null) {
			return;
		}
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
