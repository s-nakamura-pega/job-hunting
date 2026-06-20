package sn.tools.swing.xml.component;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import org.w3c.dom.Element;

import sn.tools.xml.bind.annotation.InjectXmlElement;

public class XmlList extends XmlComponent {

	private final DefaultListModel<String> model = new DefaultListModel<>();
	private final JList<String> component = new JList<>(model);

	@InjectXmlElement("item")
	public void injectItem(Element element) {
		model.addElement(element.getTextContent().trim());
	}

	@Override
	public JComponent injectTargetComponent() {
		return component;
	}

}
