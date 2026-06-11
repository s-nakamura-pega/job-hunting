package sn.tools.swing.xml.component;

import java.lang.reflect.Field;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;

public abstract class XmlButtonComponent extends XmlComponent {

	public abstract AbstractButton injectTargetButtonComponent();

	@Override
	public JComponent injectTargetComponent() {
		return injectTargetButtonComponent();
	}

	@InjectXmlAttribute("h-align")
	public void injectHorizontalAlignment(String alignment) {
		AbstractButton comp = injectTargetButtonComponent();
		try {
			Field field = AbstractButton.class.getField(alignment.toUpperCase());
			comp.setHorizontalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
		}
	}

	@InjectXmlAttribute("v-align")
	public void injectVerticalAlignment(String alignment) {
		AbstractButton comp = injectTargetButtonComponent();
		try {
			Field field = AbstractButton.class.getField(alignment.toUpperCase());
			comp.setVerticalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid vertical alignment: " + alignment, e);
		}
	}

	@InjectXmlTextContent
	public void injectLabel(String label) {
		injectTargetButtonComponent().setText(label);
	}

	@InjectXmlAttribute("selected")
	public void injectSelected(String isSelected) {
		injectTargetButtonComponent().setSelected(Boolean.parseBoolean(isSelected));
	}

	@InjectXmlAttribute("value")
	public void injectValue(String value) {
		injectTargetButtonComponent().setActionCommand(value);
	}

}
