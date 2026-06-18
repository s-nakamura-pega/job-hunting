package sn.tools.swing.flow.expansion.screen;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.flow.creator.ScreenCreator;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.util.ComponentUtils;
import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.swing.xml.injection.InjectionUtils;
import sn.tools.swing.xml.panel.XmlPanel;

public abstract class XmlScreenCreator implements ScreenCreator {

	private final Map<String, XmlComponent> componentMap = new ConcurrentHashMap<>();
	private JPanel panel;

	@Override
	public void create() {
		XmlPanel xmlPanel = CreateUtils.createXmlPanelAndPutcomponentMap(xmlURL(), componentMap);
		if (xmlPanel == null) {
			return;
		}
		Uncheck.wrapRunnable(() -> injectComponent()).run();
		onInit();
		panel = xmlPanel.injectTargetPanel();
	}

	private void injectComponent() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		InjectionUtils.injectComponent(this, componentMap);
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
	public JPanel getCreation() {
		return panel;
	}

	protected void ui(Runnable runnable) {
		ComponentUtils.operationUI(panel, runnable);
	}

	abstract protected URL xmlURL();

	abstract protected void onInit();

}
