package sn.tools.swing.flow.context;

import javax.swing.JPanel;

import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.Parameter;

public class ScreenContext implements ApplicationContext<JPanel> {

	private final FlowScreenFrame frame;

	private final Parameter parameter;

	public ScreenContext(FlowScreenFrame frame, Parameter parameter) {
		this.frame = frame;
		this.parameter = parameter;
	}

	@Override
	public Parameter parameter() {
		return parameter;
	}

	@Override
	public void flow(JPanel next) {
		frame.flowScreen(next);
	}

}
