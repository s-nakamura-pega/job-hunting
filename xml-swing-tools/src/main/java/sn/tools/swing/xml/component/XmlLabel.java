package sn.tools.swing.xml.component;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLabel;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;

public class XmlLabel extends XmlComponent {

	private final JLabel component = new JLabel();

	@InjectXmlAttribute("h-align")
	public void injectHorizontalAlignment(String alignment) {
		try {
			Field field = JLabel.class.getField(alignment.toUpperCase());
			component.setHorizontalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
		}
	}

	@InjectXmlAttribute("v-align")
	public void injectVerticalAlignment(String alignment) {
		try {
			Field field = JLabel.class.getField(alignment.toUpperCase());
			component.setVerticalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid vertical alignment: " + alignment, e);
		}
	}

	@InjectXmlTextContent
	public void injectLabel(String label) {
		component.setText(label);
	}

	@Override
	public JComponent injectTargetComponent() {
		return component;
	}

}
