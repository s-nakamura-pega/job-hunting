package sn.tools.swing.game.creator;

import sn.tools.swing.flow.creator.Creator;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.component.GameCanvas;

public abstract class SceneCreator implements Creator<AbstractCanvas> {

	private Parameter parameter;

	@Override
	public void onEnter(Parameter parameter) {
		this.parameter = parameter;
		init(parameter);
	}

	@Override
	public void onDisplay(Parameter parameter) {
		AbstractCanvas canvas = getCreation();
		if (canvas.isShowing()) {
			canvas.startLoop();
		}
	}

	@Override
	public void onExit() {
		cleanup();
		AbstractCanvas canvas = getCreation();
		canvas.stopLoop();
		if (canvas instanceof GameCanvas gc) {
			gc.clear();
		}
	}

	@Override
	public void reload() {
		onExit();
		onEnter(parameter);
		onDisplay(parameter);
	}

	protected abstract void init(Parameter parameter);

	protected abstract void cleanup();

}
