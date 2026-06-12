package sn.tools.swing.flow.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import sn.tools.clazz.creator.SimpleObjectCreator;
import sn.tools.clazz.load.PackageScanner;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableConsumer;
import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.context.ScreenContext;
import sn.tools.swing.flow.screen.ScreenCreator;

public class ScreenController {

	private final Map<String, ScreenCreator> screenMap = new ConcurrentHashMap<>();
	private final AtomicReference<String> currentScreenId = new AtomicReference<>();

	public ScreenController(String packageName) {
		Uncheck.wrapRunnable(() -> createScreen(packageName)).run();
	}

	private void createScreen(String packageName) throws ClassNotFoundException, IOException {
		Predicate<Class<?>> istarget = clazz -> {
			return !ScreenCreator.class.equals(clazz) && ScreenCreator.class.isAssignableFrom(clazz)
					&& clazz.getAnnotation(Screen.class) != null;
		};
		List<Class<?>> classList = PackageScanner.getClassList(packageName, istarget);
		if (classList.isEmpty()) {
			throw new IllegalArgumentException("画面が見つかりません。");
		}
		ThrowableConsumer<Class<?>> putMapConsumer = clazz -> {
			String screenName = clazz.getAnnotation(Screen.class).value();
			Object scObject = new SimpleObjectCreator<>(clazz).create();
			if (scObject instanceof ScreenCreator sc) {
				sc.create();
				screenMap.put(screenName, sc);
			}
		};
		classList.forEach(Uncheck.wrapConsumer(putMapConsumer));
	}

	public void flowScreen(String screenId, ScreenContext context) {
		ScreenCreator sc = screenMap.get(screenId);
		if (sc == null) {
			throw new IllegalArgumentException("ScreenIDは登録されていません。screenId: " + screenId);
		}
		String oldId = currentScreenId.getAndSet(screenId);
		if (oldId != null) {
			screenMap.get(oldId).onExit();
		}
		sc.onEnter(context);
		context.frame().flow(sc.getCreatedPanel());
	}

	public void reloadScreen() {
		screenMap.get(currentScreenId.get()).reload();
	}

}
