package sn.tools.swing.xml.component;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.w3c.dom.Element;

import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.xml.bind.annotation.InjectXmlElement;

public class XmlScroll extends XmlComponent {

	private final JScrollPane component = new JScrollPane();
	private Map<String, XmlComponent> componentMap;

	@Override
	public void setComponentMap(Map<String, XmlComponent> componentMap) {
		this.componentMap = componentMap;
	}

	@InjectXmlElement(".+")
	public void injectContent(Element element) {
		CreateUtils.createXmlComponentOrXmlPanelAndPutcomponentMap(element, componentMap)
				.ifPresent(comp -> component.setViewportView(comp.injectTargetComponent()));
	}

	@Override
	public JComponent injectTargetComponent() {
		return component;
	}

}
