package sn.tools.swing.xml.component;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import sn.tools.swing.util.WindowUtils;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlActionButton extends JButton implements XmlButtonComponent {

	private static final long serialVersionUID = 1L;

	private final AtomicReference<ActionListener> actionListener = new AtomicReference<>();

	@InjectXmlAttribute("action")
	public void setAction(String action) {
		String[] actionItems = action.split("#");
		ActionListener newAction = actionItems.length == 2 ? event -> {
			try {
				Class<?> clazz = Class.forName(actionItems[0]);
				Method method = clazz.getMethod(actionItems[1], ActionEvent.class);
				method.invoke(null, event);
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
					| InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		} : event -> {
			Window window = WindowUtils.getWindow(event);
			if (window == null) {
				return;
			}
			try {
				Method method = window.getClass().getMethod(action, ActionEvent.class);
				method.invoke(window, event);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
		ActionListener oldAction = actionListener.getAndSet(newAction);
		if (oldAction != null) {
			removeActionListener(oldAction);
		}
		addActionListener(newAction);
	}

	@Override
	public AbstractButton injectTargetButtonComponent() {
		return this;
	}

}
