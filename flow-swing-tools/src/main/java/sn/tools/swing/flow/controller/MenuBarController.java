package sn.tools.swing.flow.controller;

import java.lang.annotation.Annotation;

import javax.swing.JMenuBar;

import sn.tools.swing.flow.annotation.MenuBar;

public class MenuBarController extends Controller<JMenuBar, MenuBar> {

	public MenuBarController(String packageName) {
		super(packageName, MenuBar.class);
	}

	@Override
	protected Class<JMenuBar> getCreationClass() {
		return JMenuBar.class;
	}

	@Override
	protected String getCreatorAnnotationValue(Annotation annotation) {
		if (annotation instanceof MenuBar menuBar) {
			return menuBar.value();
		}
		throw new IllegalArgumentException(annotation.getClass().getName() + ": MenuBarアノテーションではありません。");
	}

}
