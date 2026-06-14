package sn.tools.swing.xml.injection;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;
import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.menu.XmlMenuItemComponent;

public interface InjectionUtils {

	public static void injectComponent(Object target, Map<String, XmlComponent> componentMap)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> clazz = target.getClass();
		for (Field f : clazz.getFields()) {
			InjectComponent ic = f.getAnnotation(InjectComponent.class);
			if (ic != null && componentMap.containsKey(ic.value())) {
				f.set(target, componentMap.get(ic.value()).injectTargetComponent());
			}
		}
		for (Method m : clazz.getMethods()) {
			InjectComponent ic = m.getAnnotation(InjectComponent.class);
			if (ic != null && componentMap.containsKey(ic.value())) {
				m.invoke(target, componentMap.get(ic.value()).injectTargetComponent());
			}
			InjectAction ia = m.getAnnotation(InjectAction.class);
			if (ia != null && componentMap.containsKey(ia.value())) {
				JComponent comp = componentMap.get(ia.value()).injectTargetComponent();
				if (comp instanceof AbstractButton button) {
					button.addActionListener(e -> action(target, e, m));
				}
			}
		}
	}

	public static void injectMenuItem(Object target, Map<String, XmlMenuItemComponent<?>> componentMap)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> clazz = target.getClass();
		for (Field f : clazz.getFields()) {
			InjectComponent ic = f.getAnnotation(InjectComponent.class);
			if (ic != null && componentMap.containsKey(ic.value())) {
				f.set(target, componentMap.get(ic.value()).injectTargetComponent());
			}
		}
		for (Method m : clazz.getMethods()) {
			InjectComponent ic = m.getAnnotation(InjectComponent.class);
			if (ic != null && componentMap.containsKey(ic.value())) {
				m.invoke(target, componentMap.get(ic.value()).injectTargetComponent());
			}
			InjectAction ia = m.getAnnotation(InjectAction.class);
			if (ia != null && componentMap.containsKey(ia.value())) {
				JComponent comp = componentMap.get(ia.value()).injectTargetComponent();
				if (comp instanceof JMenuItem item) {
					item.addActionListener(e -> action(target, e, m));
				}
			}
		}
	}

	private static void action(Object target, ActionEvent event, Method method) {
		Uncheck.wrapConsumer(e -> method.invoke(target, e)).accept(event);
	}

}
