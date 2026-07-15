package sn.tools.swing.flow.context;

import sn.tools.swing.flow.parameter.Parameter;

public interface ApplicationContext<T> {

	void flow(T next);

	Parameter parameter();

}
