package sn.tools.swing.xml.screen;

import java.io.ObjectInputStream.GetField;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.clazz.creator.ObjectCreator.ConstructorArgument;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;
import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.panel.XmlPanel;
import sn.tools.swing.xml.panel.XmlPanelConfigs;
import sn.tools.xml.bind.creator.XmlObjectCreator;
import sn.tools.xml.dom.DocumentUtils;
import sn.tools.xml.dom.DomElementWrapper;

public abstract class XmlScreenCreator implements XmlPanelConfigs, ScreenCreator {

	private final Map<String, XmlComponent> componentMap = new ConcurrentHashMap<>();

	@Override
	public JPanel create() {
		Document doc = DocumentUtils.read(xmlURL(), false);
		doc.getDocumentElement().normalize();
		DomElementWrapper dew = new DomElementWrapper(doc.getDocumentElement());
		Optional<Element> elementOpt = dew.childElementStream().findFirst();
		if (elementOpt.isEmpty()) {
			return new JPanel();
		}
		Element element = elementOpt.get();
		Class<? extends XmlPanel> clazz = PANEL_CONFIGS.get(element.getTagName());
		XmlPanel xmlPanel = new XmlObjectCreator<>(element, clazz).addConstructorArgument(Map.class, componentMap)
				.create();
		String id = xmlPanel.getId();
		if (id != null) {
			componentMap.put(id, xmlPanel);
		}
		Uncheck.wrapRunnable(() -> injectComponent());
		onInit();
		return xmlPanel.injectTargetPanel();
	}

	private void injectComponent() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> clazz = getClass();
		for (Field f : clazz.getFields()) {
			InjectComponent ic = f.getAnnotation(InjectComponent.class);
			if (ic != null && componentMap.containsKey(ic.value())) {
				f.set(this, componentMap.get(ic.value()).injectTargetComponent());
			}
		}
		for (Method m : clazz.getMethods()) {
			InjectComponent ic = m.getAnnotation(InjectComponent.class);
			if (ic != null && componentMap.containsKey(ic.value())) {
				m.invoke(this, componentMap.get(ic.value()).injectTargetComponent());
			}
			InjectAction ia = m.getAnnotation(InjectAction.class);
			// TODO actionインジェクション
		}
	}

	abstract protected URL xmlURL();

	abstract protected void onInit();

}
