package sn.tools.swing.xml.controller;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JFrame;

import sn.tools.clazz.creator.SimpleObjectCreator;
import sn.tools.clazz.load.PackageScanner;
import sn.tools.swing.xml.annotation.Screen;
import sn.tools.swing.xml.screen.ScreenCreator;

public interface ScreenController {

	public static void call(JFrame frame, String packageName, String screenName)
			throws ClassNotFoundException, IOException {
		Predicate<Class<?>> istarget = clazz -> {
			return !ScreenCreator.class.equals(clazz) && ScreenCreator.class.isAssignableFrom(clazz)
					&& clazz.getAnnotation(Screen.class) != null
					&& screenName.equals(clazz.getAnnotation(Screen.class).value());
		};
		List<Class<?>> classList = PackageScanner.getClassList(packageName, istarget);
		if (classList.isEmpty()) {
			throw new IllegalArgumentException("指定された画面が見つかりません: " + screenName);
		}
		if (classList.size() > 1) {
			throw new IllegalStateException("画面名 '" + screenName + "' が重複しています: " + classList);
		}
		Object scObject = new SimpleObjectCreator<>(classList.getFirst()).create();
		if (scObject instanceof ScreenCreator sc) {
			frame.setContentPane(sc.create());
		}
	}

}
