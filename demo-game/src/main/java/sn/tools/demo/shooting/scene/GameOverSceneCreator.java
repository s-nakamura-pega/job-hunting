package sn.tools.demo.shooting.scene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sn.tools.demo.shooting.canvas.GameOverCanvas;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.creator.SceneCreator;
import sn.tools.swing.game.panel.GamePanel;
import sn.tools.swing.util.KeyUtils.KeyAction;
import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

@Scene("gameover")
public class GameOverSceneCreator extends SceneCreator {

	private GameOverCanvas canvas;

	@Override
	public void create() {
		canvas = new GameOverCanvas();
		canvas.addKeyAction(new KeyAction("flowTitle", _ -> flowTitle(), FocusTargetCondition.WINDOW_AND_COMPONENT,
				KeyEvent.VK_ENTER, false, KeyModifiers.NONE));
		canvas.addMouseListenerEx(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				flowTitle();
			}

		});
	}

	private void flowTitle() {
		GamePanel.flowScene(canvas, "title", new SimpleParameter());
	}

	@Override
	public AbstractCanvas getCreation() {
		return canvas;
	}

	@Override
	protected void init(Parameter parameter) {
	}

	@Override
	protected void cleanup() {
	}

}
