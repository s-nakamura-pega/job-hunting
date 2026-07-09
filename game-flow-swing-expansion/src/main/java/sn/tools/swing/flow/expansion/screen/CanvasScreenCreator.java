package sn.tools.swing.flow.expansion.screen;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import sn.tools.swing.flow.creator.ScreenCreator;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.game.component.AbstractCanvas;

public abstract class CanvasScreenCreator<T extends AbstractCanvas> implements ScreenCreator {

	private JPanel panel;

	@Override
	public void create() {
		this.panel = new JPanel(new BorderLayout());
		AbstractCanvas canvas = canvas();
		panel.add(canvas);
		canvas.setFps(fps());
		OnInit();
	}

	@Override
	public void onEnter(ScreenParameter parameter) {
	}

	@Override
	public void onDisplay(ScreenParameter parameter) {
		canvas().startLoop();
	}

	@Override
	public void onExit() {
		canvas().stopLoop();
	}

	@Override
	public void reload() {
	}

	@Override
	public JPanel getCreation() {
		return panel;
	}

	protected abstract int fps();

	protected abstract T canvas();

	protected abstract void OnInit();

}
