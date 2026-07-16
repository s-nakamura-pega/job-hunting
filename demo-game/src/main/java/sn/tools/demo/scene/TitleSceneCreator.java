package sn.tools.demo.scene;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sn.tools.demo.canvas.TitleCanvas;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.creator.SceneCreator;
import sn.tools.swing.game.panel.GamePanel;

@Scene("title")
public class TitleSceneCreator implements SceneCreator {

	private TitleCanvas canvas;

	@Override
	public void create() {
		canvas = new TitleCanvas();
		canvas.addMouseListenerEx(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				GamePanel.flowScene(canvas, "game", new SimpleParameter());
			}

		});
	}

	@Override
	public void onEnter(Parameter parameter) {
	}

	@Override
	public void onDisplay(Parameter parameter) {
		canvas.startLoop();
	}

	@Override
	public void onExit() {
		canvas.stopLoop();
	}

	@Override
	public void reload() {
	}

	@Override
	public AbstractCanvas getCreation() {
		return canvas;
	}
}
