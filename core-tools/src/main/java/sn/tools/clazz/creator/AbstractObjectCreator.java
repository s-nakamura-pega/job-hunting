package sn.tools.clazz.creator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ExceptionHandler;
import sn.tools.function.uncheck.Uncheck.ThrowableSupplier;

public abstract class AbstractObjectCreator<T> implements ObjectCreator<T> {

	private final Class<T> clazz;
	private final List<ConstructorArgument<?>> constructorArgs = new ArrayList<>();

	public AbstractObjectCreator(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<T> getCreateClass() {
		return clazz;
	}

	@Override
	public T create() {
		ThrowableSupplier<T> supplier = () -> {
			Constructor<T> constructor = clazz
					.getConstructor(constructorArgs.stream().map(ConstructorArgument::clazz).toArray(Class<?>[]::new));
			Object[] values = constructorArgs.stream().map(ConstructorArgument::value).toArray();
			T instance = constructor.newInstance(values);
			decorate(instance);
			return instance;
		};
		ExceptionHandler<T> handler = e -> {
			throw new RuntimeException(ExceptionUtils.getRootCause(e));
		};
		return Uncheck.wrapSupplier(supplier, handler).get();
	}

	public <R> AbstractObjectCreator<T> addConstructorArgument(Class<R> clazz, R value) {
		constructorArgs.add(new ConstructorArgument<>(clazz, value));
		return this;
	}

	public <R> AbstractObjectCreator<T> addConstructorArgument(ConstructorArgument<?> arg) {
		constructorArgs.add(arg);
		return this;
	}

	public <R> AbstractObjectCreator<T> addAllConstructorArgument(Collection<? extends ConstructorArgument<?>> args) {
		constructorArgs.addAll(args);
		return this;
	}

	protected abstract void decorate(T instance) throws Exception;

}
