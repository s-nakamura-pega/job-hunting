package sn.tools.swing.xml.panel;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.w3c.dom.Element;

import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.component.XmlComponentConfigs;
import sn.tools.xml.bind.annotation.InjectXmlElement;

public interface XmlPanel extends XmlComponentConfigs, XmlComponent, XmlPanelConfigs {

	@InjectXmlElement({ "text", "button", "check-box", "radio-button", "text-area", "label" })
	default void addComponent(Element element) {
		injectTargetPanel()
				.add(COMPONENT_CONFIGS.get(element.getTagName()).getComponent(element).injectTargetComponent());
	}

	default void addPanel(Element element) {
		injectTargetPanel().add(PANEL_CONFIGS.get(element.getTagName()).getPanel(element).injectTargetPanel());
	}

	JPanel injectTargetPanel();

	@Override
	default JComponent injectTargetComponent() {
		return injectTargetPanel();
	}

}
