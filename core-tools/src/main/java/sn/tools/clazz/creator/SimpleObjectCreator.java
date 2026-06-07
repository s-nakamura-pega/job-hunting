package sn.tools.clazz.creator;

public class SimpleObjectCreator<T> extends AbstractObjectCreator<T> {

	public SimpleObjectCreator(Class<T> clazz) {
		super(clazz);
	}

	@Override
	protected void decorate(T instance) throws Exception {
	}

}
