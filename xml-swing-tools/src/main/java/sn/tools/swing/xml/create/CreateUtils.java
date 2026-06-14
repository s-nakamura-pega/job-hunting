package sn.tools.swing.xml.create;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.menu.XmlMenuBar;
import sn.tools.swing.xml.menu.XmlMenuItemComponent;
import sn.tools.swing.xml.panel.XmlPanel;
import sn.tools.swing.xml.panel.XmlPanelConfigs;
import sn.tools.xml.bind.creator.XmlObjectCreator;
import sn.tools.xml.dom.DocumentUtils;
import sn.tools.xml.dom.DomElementWrapper;

public interface CreateUtils extends XmlPanelConfigs {

	public static XmlPanel createXmlPanel(URL xmlURL, Map<String, XmlComponent> componentMap) {
		Document doc = DocumentUtils.read(xmlURL, true);
		doc.getDocumentElement().normalize();
		DomElementWrapper dew = new DomElementWrapper(doc.getDocumentElement());
		Optional<Element> elementOpt = dew.childElementStream().findFirst();
		if (elementOpt.isEmpty()) {
			return null;
		}
		Element element = elementOpt.get();
		Class<? extends XmlPanel> clazz = PANEL_CONFIGS.get(element.getTagName());
		XmlObjectCreator<? extends XmlPanel> xoc = new XmlObjectCreator<>(element, clazz);
		xoc.setPreDecorateProcess(panel -> panel.setComponentMap(componentMap));
		return xoc.create();
	}

	public static XmlMenuBar createXmlMenuBar(URL xmlURL, Map<String, XmlMenuItemComponent<?>> componentMap) {
		Document doc = DocumentUtils.read(xmlURL, true);
		doc.getDocumentElement().normalize();
		XmlObjectCreator<XmlMenuBar> xoc = new XmlObjectCreator<>(doc.getDocumentElement(), XmlMenuBar.class);
		xoc.setPreDecorateProcess(menuBar -> menuBar.setComponentMap(componentMap));
		return xoc.create();
	}

}
