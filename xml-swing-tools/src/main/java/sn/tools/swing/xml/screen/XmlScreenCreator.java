package sn.tools.swing.xml.screen;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.flow.screen.ScreenCreator;
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
	private JPanel panel;

	@Override
	public void create() {
		Document doc = DocumentUtils.read(xmlURL(), true);
		doc.getDocumentElement().normalize();
		DomElementWrapper dew = new DomElementWrapper(doc.getDocumentElement());
		Optional<Element> elementOpt = dew.childElementStream().findFirst();
		if (elementOpt.isEmpty()) {
			return;
		}
		Element element = elementOpt.get();
		Class<? extends XmlPanel> clazz = PANEL_CONFIGS.get(element.getTagName());
		XmlObjectCreator<? extends XmlPanel> xoc = new XmlObjectCreator<>(element, clazz);
		xoc.setPreDecorateProcess(panel -> panel.setComponentMap(componentMap));
		XmlPanel xmlPanel = xoc.create();
		String id = xmlPanel.getId();
		if (id != null) {
			componentMap.put(id, xmlPanel);
		}
		Uncheck.wrapRunnable(() -> injectComponent()).run();
		onInit();
		panel = xmlPanel.injectTargetPanel();
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
			if (ia != null && componentMap.containsKey(ia.value())) {
				JComponent comp = componentMap.get(ia.value()).injectTargetComponent();
				if (comp instanceof AbstractButton button) {
					button.addActionListener(e -> action(e, m));
				}
			}
		}
	}

	private void action(ActionEvent event, Method method) {
		Uncheck.wrapConsumer(e -> method.invoke(this, e)).accept(event);
	}

	@Override
	public JPanel getCreatedPanel() {
		return panel;
	}

	@Override
	public <T extends JPanel> T getCreatedPanel(Class<T> clazz) {
		if (clazz.isInstance(panel)) {
			return clazz.cast(panel);
		}
		return null;
	}

	abstract protected URL xmlURL();

	abstract protected void onInit();

}
