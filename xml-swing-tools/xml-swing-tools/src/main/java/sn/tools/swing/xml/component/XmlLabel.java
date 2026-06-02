package sn.tools.swing.xml.component;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLabel;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;

public class XmlLabel extends JLabel implements XmlComponent {

	private static final long serialVersionUID = 1L;

	@InjectXmlAttribute("h-align")
	public void injectHorizontalAlignment(String alignment) {
		try {
			Field field = JLabel.class.getField(alignment.toUpperCase());
			setHorizontalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
		}
	}

	@InjectXmlAttribute("v-align")
	public void injectVerticalAlignment(String alignment) {
		try {
			Field field = JLabel.class.getField(alignment.toUpperCase());
			setVerticalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid vertical alignment: " + alignment, e);
		}
	}

	@InjectXmlTextContent
	public void injectLabel(String label) {
		setText(label);
	}

	@Override
	public JComponent injectTargetComponent() {
		return this;
	}

}
