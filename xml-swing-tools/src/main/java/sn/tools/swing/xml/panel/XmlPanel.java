package sn.tools.swing.xml.panel;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.w3c.dom.Element;

import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.component.XmlComponentConfigs;
import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.xml.bind.annotation.InjectXmlElement;

public abstract class XmlPanel extends XmlComponent implements XmlComponentConfigs, XmlPanelConfigs {

	protected Map<String, XmlComponent> componentMap;

	@Override
	public void setComponentMap(Map<String, XmlComponent> componentMap) {
		this.componentMap = componentMap;
	}

	@InjectXmlElement(".+")
	public void addComponent(Element element) {
		CreateUtils.createXmlComponentOrXmlPanelAndPutcomponentMap(element, componentMap)
				.ifPresent(comp -> injectTargetPanel().add(comp.injectTargetComponent()));
	}

	public abstract JPanel injectTargetPanel();

	@Override
	public JComponent injectTargetComponent() {
		return injectTargetPanel();
	}

}
