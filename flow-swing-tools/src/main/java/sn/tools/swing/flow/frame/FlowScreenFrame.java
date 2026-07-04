package sn.tools.swing.flow.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

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

	private MenuBarController menuController;
	private ScreenController screenController;

	public FlowScreenFrame() {
		super();
		long waitTime = 3000;
		Date start = new Date();
		setSize(WindowUtils.getScreenRatioSize(0.7));
		setLocationRelativeTo(null);
		Timer titleTimer = setTitle();
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
				Timer timer = new Timer((int) (procTime < waitTime ? waitTime - procTime : 0), _ -> {
					try {
						titleTimer.stop();
						get();
						flowMenuBar(initMenuBarId(), new SimpleScreenParameter());
						flowScreen(initScreenId(), new SimpleScreenParameter());
						onInit();
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				});
				timer.setRepeats(false);
				timer.start();
			}

		};
		worker.execute();
	}

	private Timer setTitle() {
		JLabel label = new JLabel("Swing Framework");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("SansSerif", Font.PLAIN, 40));
		label.setForeground(Color.WHITE);
		label.setBackground(Color.DARK_GRAY);
		label.setOpaque(true);
		getContentPane().add(label);
		JLabel proc = new JLabel(" Loading.");
		proc.setFont(new Font("SansSerif", Font.PLAIN, 20));
		proc.setForeground(Color.WHITE);
		proc.setBackground(Color.DARK_GRAY);
		proc.setOpaque(true);
		proc.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(proc, BorderLayout.SOUTH);
		AtomicInteger index = new AtomicInteger(0);
		String[] dots = { ".", "..", "...", "...." };
		Timer timer = new Timer(500, _ -> {
			int i = index.getAndUpdate(v -> (v + 1) % dots.length);
			proc.setText(" Loading" + dots[i]);
		});
		timer.start();
		return timer;
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
