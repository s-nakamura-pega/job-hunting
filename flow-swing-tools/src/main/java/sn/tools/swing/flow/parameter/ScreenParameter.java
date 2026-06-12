package sn.tools.swing.flow.parameter;

import java.util.Optional;

public interface ScreenParameter {

	Optional<?> getParam(String key);

	<T> Optional<T> getParam(String key, Class<T> clazz);

}
