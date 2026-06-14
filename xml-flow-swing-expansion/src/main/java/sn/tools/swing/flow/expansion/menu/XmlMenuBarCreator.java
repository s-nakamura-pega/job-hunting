package sn.tools.swing.flow.expansion.menu;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JMenuBar;

import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.flow.annotation.MenuBar;
import sn.tools.swing.flow.creator.MenuBarCreator;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.swing.xml.injection.InjectionUtils;
import sn.tools.swing.xml.menu.XmlMenuBar;
import sn.tools.swing.xml.menu.XmlMenuItemComponent;

@MenuBar("init")
public abstract class XmlMenuBarCreator implements MenuBarCreator {

	private final Map<String, XmlMenuItemComponent<?>> componentMap = new ConcurrentHashMap<>();
	private JMenuBar menuBar;

	@Override
	public void create() {
		XmlMenuBar xmlMenuBar = CreateUtils.createXmlMenuBar(xmlURL(), componentMap);
		if (xmlMenuBar == null) {
			return;
		}
		Uncheck.wrapRunnable(() -> injectComponent()).run();
		onInit();
		menuBar = xmlMenuBar.injectTargetMenuBar();
	}

	private void injectComponent() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		InjectionUtils.injectMenuItem(this, componentMap);
	}

	@Override
	public void reload() {
	}

	@Override
	public void onEnter(ScreenParameter parameter) {
	}

	@Override
	public void onExit() {
	}

	@Override
	public JMenuBar getCreation() {
		return menuBar;
	}

	abstract protected URL xmlURL();

	abstract protected void onInit();

}
