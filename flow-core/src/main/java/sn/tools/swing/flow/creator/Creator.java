package sn.tools.swing.flow.creator;

import sn.tools.swing.flow.parameter.Parameter;

public interface Creator<T> {
	
	void create();

	void onEnter(Parameter parameter);
	
	void onDisplay(Parameter parameter);

	void onExit();

	void reload();

	T getCreation();

	default <R extends T> R getCreation(Class<R> clazz) {
		T creation = getCreation();
		if (clazz.isInstance(creation)) {
			return clazz.cast(creation);
		}
		return null;
	}

}
