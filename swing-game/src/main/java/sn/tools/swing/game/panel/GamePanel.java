package sn.tools.swing.game.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.context.SceneContext;
import sn.tools.swing.game.controller.SceneController;

public abstract class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final BorderLayout layout = new BorderLayout();

	private SceneController controller;

	public GamePanel() {
		super.setLayout(layout);
		SwingWorker<Void, String> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws Exception {
				controller = new SceneController(scanScenePackage());
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
			}

			@Override
			protected void done() {
				try {
					get();
					flowScene(initSceneId(), new SimpleParameter());
					onInit();
				} catch (Exception ex) {
					ex.printStackTrace();
					Exception root = ExceptionUtils.getRootCause(ex);
					JOptionPane.showMessageDialog(GamePanel.this, root.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		worker.execute();
	}

	public void flowScene(String screenId, Parameter parameter) {
		controller.flow(screenId, new SceneContext(this, parameter));
	}

	public void flowScene(AbstractCanvas next) {
		AbstractCanvas current = controller.getCurrentCanvas();
		if (current != null) {
			super.remove(current);
		}
		super.add(next, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	@Override
	public Component add(Component comp) {
		throw new UnsupportedOperationException("GamePanel は add を直接使えません。flowScene を使ってください。");
	}

	@Override
	public void remove(Component comp) {
		throw new UnsupportedOperationException("GamePanel は remove を直接使えません。Scene 切り替えは flowScene が管理します。");
	}

	@Override
	public void setLayout(LayoutManager mgr) {
		throw new UnsupportedOperationException("GamePanel は setLayout を直接使えません。Layout は flowScene が管理します。");
	}

	public void startGame() {
		controller.start();
	}

	public void stopGame() {
		controller.stop();
	}

	protected abstract String scanScenePackage();

	protected abstract String initSceneId();

	protected abstract void onInit();

	public static void flowScene(JComponent component, String screenId, Parameter parameter) {
		if (component.getParent() instanceof GamePanel gp) {
			gp.flowScene(screenId, parameter);
		}
	}

}
