package sn.tools.swing.flow.context;

import sn.tools.swing.flow.parameter.ScreenParameter;

public interface ApplicationContext<T> {

	void flow(T next);

	ScreenParameter parameter();

}
