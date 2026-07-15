package sn.tools.swing.flow.controller;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import sn.tools.clazz.creator.SimpleObjectCreator;
import sn.tools.clazz.load.PackageScanner;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableConsumer;
import sn.tools.swing.flow.context.ApplicationContext;
import sn.tools.swing.flow.creator.Creator;

public abstract class Controller<T, A extends Annotation> {

	protected final Map<String, Creator<T>> creatorMap = new ConcurrentHashMap<>();
	protected final AtomicReference<String> currentId = new AtomicReference<>();

	public Controller(String packageName, Class<A> annotationClazz) {
		Uncheck.wrapRunnable(() -> create(packageName, annotationClazz)).run();
	}

	private void create(String packageName, Class<A> annotationClazz) throws ClassNotFoundException, IOException {
		Predicate<Class<?>> istarget = clazz -> {
			return !Creator.class.equals(clazz) && Creator.class.isAssignableFrom(clazz)
					&& clazz.getAnnotation(annotationClazz) != null;
		};
		List<Class<?>> classList = PackageScanner.getClassList(packageName, istarget);
		if (classList.isEmpty()) {
			throw new IllegalArgumentException(
					getClass().getSimpleName() + ": " + annotationClazz.getSimpleName() + "が見つかりません。");
		}
		@SuppressWarnings("unchecked")
		ThrowableConsumer<Class<?>> putMapConsumer = clazz -> {
			String screenName = getCreatorAnnotationValue(clazz.getAnnotation(annotationClazz));
			Object scObject = new SimpleObjectCreator<>(clazz).create();
			if (scObject instanceof Creator creator) {
				creator.create();
				if (getCreationClass().isInstance(creator.getCreation())) {
					creatorMap.put(screenName, creator);
				}
			}
		};
		classList.forEach(Uncheck.wrapConsumer(putMapConsumer));
	}

	public void flow(String id, ApplicationContext<T> context) {
		Creator<T> creator = creatorMap.get(id);
		if (creator == null) {
			throw new IllegalArgumentException(getClass().getSimpleName() + ": IDが登録されていません。ID=" + id);
		}
		creator.onEnter(context.parameter());
		context.flow(creator.getCreation());
		creator.onDisplay(context.parameter());
		String oldId = currentId.getAndSet(id);
		if (oldId != null) {
			SwingUtilities.invokeLater(() -> creatorMap.get(oldId).onExit());
		}
	}

	public void reloadScreen() {
		creatorMap.get(currentId.get()).reload();
	}

	protected abstract String getCreatorAnnotationValue(Annotation annotation);

	protected abstract Class<T> getCreationClass();

}
