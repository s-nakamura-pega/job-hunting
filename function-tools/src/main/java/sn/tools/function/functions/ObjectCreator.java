package sn.tools.function.functions;

public interface ObjectCreator<T> {

	Class<T> getCreateClass();

	T create();

	public static record ConstructorArgument<R>(Class<R> clazz, R value) {
	}

}
