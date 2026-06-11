package sn.tools.swing.flow.parameter;

import java.util.HashMap;
import java.util.Map;

public class SimpleScreenParameter implements ScreenParameter {

	private final Map<String, Object> params = new HashMap<>();

	@Override
	public void addParam(String key, Object value) {
		params.put(key, value);
	}

	@Override
	public Object getParam(String key) {
		return params.get(key);
	}

}
