package sn.tools.swing.xml.menu;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JMenuItem;

import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableRunnable;
import sn.tools.function.uncheck.Uncheck.VoidExceptionHandler;
import sn.tools.swing.util.WindowUtils;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlMenuItem extends XmlMenuItemComponent<JMenuItem> {

	private final JMenuItem component = new JMenuItem();

	private final AtomicReference<ActionListener> actionListener = new AtomicReference<>();

	@InjectXmlAttribute("action")
	public void setAction(String action) {
		String[] actionItems = action.split("#");
		ActionListener newAction = actionItems.length == 2 ? event -> {
			ThrowableRunnable runnable = () -> {
				Class<?> clazz = Class.forName(actionItems[0]);
				Method method = clazz.getMethod(actionItems[1], ActionEvent.class);
				method.invoke(null, event);
			};
			VoidExceptionHandler handler = e -> {
				throw new RuntimeException(ExceptionUtils.getRootCause(e));
			};
			Uncheck.wrapRunnable(runnable, handler).run();
		} : event -> {
			Window window = WindowUtils.getWindow(event);
			if (window == null) {
				return;
			}
			ThrowableRunnable runnable = () -> {
				Method method = window.getClass().getMethod(action, ActionEvent.class);
				method.invoke(window, event);
			};
			VoidExceptionHandler handler = e -> {
				throw new RuntimeException(ExceptionUtils.getRootCause(e));
			};
			Uncheck.wrapRunnable(runnable, handler).run();
		};
		ActionListener oldAction = actionListener.getAndSet(newAction);
		if (oldAction != null) {
			component.removeActionListener(oldAction);
		}
		component.addActionListener(newAction);
	}

	@Override
	public JMenuItem injectTargetComponent() {
		return component;
	}

}
