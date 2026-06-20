package sn.tools.swing.xml.component;

import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.XmlObject;

@XmlObject("password")
public class XmlPasswordField extends XmlTextComponent {

	private final JPasswordField component = new JPasswordField();

	@InjectXmlAttribute("columns")
	public void injectColumns(String value) {
		component.setColumns(Integer.parseInt(value));
	}

	@Override
	public JTextComponent injectTargetTextComponent() {
		return component;
	}

}
