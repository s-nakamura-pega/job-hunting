package sn.tools.swing.flow.context;

import javax.swing.JMenuBar;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;

public class MenuBarContext implements ApplicationContext<JMenuBar> {

	private final FlowScreenFrame frame;

	private final ScreenParameter parameter;

	public MenuBarContext(FlowScreenFrame frame, ScreenParameter parameter) {
		this.frame = frame;
		this.parameter = parameter;
	}

	@Override
	public ScreenParameter parameter() {
		return parameter;
	}

	@Override
	public void flow(JMenuBar next) {
		frame.flowMenuBar(next);
	}

}
