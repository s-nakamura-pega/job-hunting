package sn.tools.swing.flow.context;

import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;

public class SimpleScreenContext implements ScreenContext {

	private final FlowScreenFrame frame;

	private final ScreenParameter parameter;

	public SimpleScreenContext(FlowScreenFrame frame, ScreenParameter parameter) {
		this.frame = frame;
		this.parameter = parameter;
	}

	@Override
	public FlowScreenFrame frame() {
		return frame;
	}

	@Override
	public ScreenParameter parameter() {
		return parameter;
	}

}
