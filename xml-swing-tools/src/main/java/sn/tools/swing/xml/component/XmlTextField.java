package sn.tools.swing.xml.component;

import java.lang.reflect.Field;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.XmlObject;

@XmlObject("text")
public class XmlTextField extends XmlTextComponent {

	private static final long serialVersionUID = 1L;

	private final JTextField component = new JTextField();

	@InjectXmlAttribute("h-align")
	public void injectHorizontalAlignment(String alignment) {
		try {
			Field field = JTextField.class.getField(alignment.toUpperCase());
			component.setHorizontalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
		}
	}

	public JTextComponent injectTargetTextComponent() {
		return component;
	}

}
