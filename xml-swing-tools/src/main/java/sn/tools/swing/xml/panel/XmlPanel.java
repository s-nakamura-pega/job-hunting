package sn.tools.swing.xml.panel;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.w3c.dom.Element;

import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.component.XmlComponentConfigs;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public abstract class XmlPanel extends XmlComponent implements XmlComponentConfigs, XmlPanelConfigs {

	protected Map<String, XmlComponent> componentMap;

	public void setComponentMap(Map<String, XmlComponent> componentMap) {
		this.componentMap = componentMap;
	}

	@InjectXmlElement({ "text", "button", "check-box", "radio-button", "text-area", "label" })
	public void addComponent(Element element) {
		Class<? extends XmlComponent> clazz = COMPONENT_CONFIGS.get(element.getTagName());
		XmlComponent comp = new XmlObjectCreator<>(element, clazz).create();
		String id = comp.getId();
		if (id != null) {
			componentMap.put(id, comp);
		}
		injectTargetPanel().add(comp.injectTargetComponent());
	}

	@InjectXmlElement({ "flow-panel" })
	public void addPanel(Element element) {
		Class<? extends XmlPanel> clazz = PANEL_CONFIGS.get(element.getTagName());
		XmlObjectCreator<? extends XmlPanel> xoc = new XmlObjectCreator<>(element, clazz);
		xoc.setPreDecorateProcess(panel -> panel.setComponentMap(componentMap));
		XmlPanel xmlPanel = xoc.create();
		String id = xmlPanel.getId();
		if (id != null) {
			componentMap.put(id, xmlPanel);
		}
		injectTargetPanel().add(xmlPanel.injectTargetPanel());
	}

	public abstract JPanel injectTargetPanel();

	@Override
	public JComponent injectTargetComponent() {
		return injectTargetPanel();
	}

}
