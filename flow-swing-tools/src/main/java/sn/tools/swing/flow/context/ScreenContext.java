package sn.tools.swing.flow.context;

import javax.swing.JPanel;

import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;

public class ScreenContext implements ApplicationContext<JPanel> {

	private final FlowScreenFrame frame;

	private final ScreenParameter parameter;

	public ScreenContext(FlowScreenFrame frame, ScreenParameter parameter) {
		this.frame = frame;
		this.parameter = parameter;
	}

	@Override
	public ScreenParameter parameter() {
		return parameter;
	}

	@Override
	public void flow(JPanel next) {
		frame.flowScreen(next);
	}

}
