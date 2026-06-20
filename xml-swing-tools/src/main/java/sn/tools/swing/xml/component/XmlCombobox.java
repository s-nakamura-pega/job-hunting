package sn.tools.swing.xml.component;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.w3c.dom.Element;

import sn.tools.xml.bind.annotation.InjectXmlElement;

public class XmlCombobox extends XmlComponent {

	private final JComboBox<String> component = new JComboBox<>();

	@InjectXmlElement("item")
	public void injectItem(Element element) {
		component.addItem(element.getTextContent().trim());
	}

	@Override
	public JComponent injectTargetComponent() {
		return component;
	}

}
