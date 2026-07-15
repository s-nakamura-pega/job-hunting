package sn.tools.swing.game.context;

import sn.tools.swing.flow.context.ApplicationContext;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.panel.GamePanel;

public class SceneContext implements ApplicationContext<AbstractCanvas> {

	private final GamePanel panel;

	private final Parameter parameter;

	public SceneContext(GamePanel panel, Parameter parameter) {
		this.panel = panel;
		this.parameter = parameter;
	}

	@Override
	public Parameter parameter() {
		return parameter;
	}

	@Override
	public void flow(AbstractCanvas next) {
		panel.flowScene(next);
	}

}
