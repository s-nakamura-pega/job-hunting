package sn.tools.swing.flow.context;

import javax.swing.JMenuBar;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.Parameter;

public class MenuBarContext implements ApplicationContext<JMenuBar> {

	private final FlowScreenFrame frame;

	private final Parameter parameter;

	public MenuBarContext(FlowScreenFrame frame, Parameter parameter) {
		this.frame = frame;
		this.parameter = parameter;
	}

	@Override
	public Parameter parameter() {
		return parameter;
	}

	@Override
	public void flow(JMenuBar next) {
		frame.flowMenuBar(next);
	}

}
