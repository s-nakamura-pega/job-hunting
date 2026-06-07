package sn.tools.db.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class DBResponse {

	private final Map<Class<?>, Object> responseMap = new HashMap<>();

	public void put(Class<?> clazz, Object value) {
		if (value == null) {
			return;
		}
		if (!clazz.isInstance(value)) {
			throw new IllegalArgumentException(String.format("keyとvalueの型が異なります。[key-class: %s, value-class: %s]",
					clazz.getName(), value.getClass().getName()));
		}
		responseMap.put(clazz, value);
	}

	public <T> T get(Class<T> clazz) {
		return Optional.ofNullable(responseMap.get(clazz))
	            .filter(clazz::isInstance)
	            .map(clazz::cast)
	            .orElse(null);
	}

	public <R> R convert(Function<Map<Class<?>, Object>, R> convertFunction) {
		return convertFunction.apply(Map.copyOf(responseMap));
	}

}
