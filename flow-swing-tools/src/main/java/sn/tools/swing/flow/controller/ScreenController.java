package sn.tools.swing.flow.controller;

import java.lang.annotation.Annotation;

import javax.swing.JPanel;

import sn.tools.swing.flow.annotation.Screen;

public class ScreenController extends Controller<JPanel, Screen> {

	public ScreenController(String packageName) {
		super(packageName, Screen.class);
	}

	@Override
	protected Class<JPanel> getCreationClass() {
		return JPanel.class;
	}

	@Override
	protected String getCreatorAnnotationValue(Annotation annotation) {
		if (annotation instanceof Screen screen) {
			return screen.value();
		}
		throw new IllegalArgumentException(annotation.getClass().getName() + ": Screenアノテーションではありません。");
	}

}
