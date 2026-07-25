package sn.tools.demo.shooting.scene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sn.tools.demo.shooting.canvas.TitleCanvas;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.creator.SceneCreator;
import sn.tools.swing.game.panel.GamePanel;
import sn.tools.swing.util.KeyUtils.KeyAction;
import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

@Scene("title")
public class TitleSceneCreator extends SceneCreator {

	private TitleCanvas canvas;

	@Override
	public void create() {
		canvas = new TitleCanvas();
		canvas.addKeyAction(new KeyAction("flowGame", _ -> flowGame(), FocusTargetCondition.WINDOW_AND_COMPONENT,
				KeyEvent.VK_ENTER, false, KeyModifiers.NONE));
		canvas.addMouseListenerEx(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				flowGame();
			}

		});
	}

	private void flowGame() {
		GamePanel.flowScene(canvas, "game", new SimpleParameter());
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
