package sn.tools.swing.flow.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleScreenParameter implements ScreenParameter {

	private final Map<String, Object> params = new HashMap<>();

	public void addParam(String key, Object value) {
		params.put(key, value);
	}

	@Override
	public Optional<?> getParam(String key) {
		return Optional.ofNullable(params.get(key));
	}

	@Override
	public <T> Optional<T> getParam(String key, Class<T> clazz) {
		Object value = params.get(key);
		if (value != null && clazz.isInstance(value)) {
			return Optional.of(clazz.cast(value));
		}
		return Optional.empty();
	}

}
