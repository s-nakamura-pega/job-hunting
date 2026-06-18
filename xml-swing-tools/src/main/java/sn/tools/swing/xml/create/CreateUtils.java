package sn.tools.swing.xml.create;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.component.XmlComponentConfigs;
import sn.tools.swing.xml.menu.XmlMenuBar;
import sn.tools.swing.xml.menu.XmlMenuItemComponent;
import sn.tools.swing.xml.panel.XmlPanel;
import sn.tools.swing.xml.panel.XmlPanelConfigs;
import sn.tools.xml.bind.creator.XmlObjectCreator;
import sn.tools.xml.dom.DocumentUtils;
import sn.tools.xml.dom.DomElementWrapper;

public interface CreateUtils extends XmlPanelConfigs, XmlComponentConfigs {

	public static XmlPanel createXmlPanelAndPutcomponentMap(URL xmlURL, Map<String, XmlComponent> componentMap) {
		Document doc = DocumentUtils.read(xmlURL, true);
		doc.getDocumentElement().normalize();
		DomElementWrapper dew = new DomElementWrapper(doc.getDocumentElement());
		Optional<Element> elementOpt = dew.childElementStream().findFirst();
		if (elementOpt.isEmpty()) {
			return null;
		}
		return createXmlPanelAndPutcomponentMap(elementOpt.get(), componentMap);
	}

	public static XmlPanel createXmlPanelAndPutcomponentMap(Element element, Map<String, XmlComponent> componentMap) {
		Class<? extends XmlPanel> clazz = PANEL_CONFIGS.get(element.getLocalName());
		XmlObjectCreator<? extends XmlPanel> xoc = new XmlObjectCreator<>(element, clazz);
		xoc.setPreDecorateProcess(panel -> panel.setComponentMap(componentMap));
		XmlPanel xmlPanel = xoc.create();
		String id = xmlPanel.getId();
		if (id != null) {
			componentMap.put(id, xmlPanel);
		}
		return xmlPanel;
	}

	public static XmlComponent createXmlComponentAndPutcomponentMap(Element element,
			Map<String, XmlComponent> componentMap) {
		Class<? extends XmlComponent> clazz = COMPONENT_CONFIGS.get(element.getLocalName());
		XmlObjectCreator<? extends XmlComponent> xoc = new XmlObjectCreator<>(element, clazz);
		xoc.setPreDecorateProcess(pane -> pane.setComponentMap(componentMap));
		XmlComponent comp = xoc.create();
		String id = comp.getId();
		if (id != null) {
			componentMap.put(id, comp);
		}
		return comp;
	}

	public static Optional<XmlComponent> createXmlComponentOrXmlPanelAndPutcomponentMap(Element element,
			Map<String, XmlComponent> componentMap) {
		XmlComponent component = null;
		if (PANEL_CONFIGS.containsKey(element.getLocalName())) {
			component = createXmlPanelAndPutcomponentMap(element, componentMap);
		} else if (COMPONENT_CONFIGS.containsKey(element.getLocalName())) {
			component = createXmlComponentAndPutcomponentMap(element, componentMap);
		}
		return Optional.ofNullable(component);
	}

	public static XmlMenuBar createXmlMenuBar(URL xmlURL, Map<String, XmlMenuItemComponent<?>> componentMap) {
		Document doc = DocumentUtils.read(xmlURL, true);
		doc.getDocumentElement().normalize();
		return createXmlMenuBar(doc.getDocumentElement(), componentMap);
	}

	public static XmlMenuBar createXmlMenuBar(Element element, Map<String, XmlMenuItemComponent<?>> componentMap) {
		XmlObjectCreator<XmlMenuBar> xoc = new XmlObjectCreator<>(element, XmlMenuBar.class);
		xoc.setPreDecorateProcess(menuBar -> menuBar.setComponentMap(componentMap));
		return xoc.create();
	}

}
