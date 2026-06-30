package sn.tools.swing.xml.component;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlTextArea extends XmlTextComponent {

	private final JTextArea component = new JTextArea();

	@InjectXmlAttribute("editable")
	public void setEditable(String editable) {
		component.setEditable(Boolean.getBoolean(editable));
	}

	@Override
	public JTextComponent injectTargetTextComponent() {
		return component;
	}

}
