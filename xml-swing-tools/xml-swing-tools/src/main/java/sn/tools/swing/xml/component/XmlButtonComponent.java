package sn.tools.swing.xml.component;

import java.lang.reflect.Field;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public interface XmlButtonComponent extends XmlComponent {

	AbstractButton injectTargetButtonComponent();

	@Override
	default JComponent injectTargetComponent() {
		return injectTargetButtonComponent();
	}

	@InjectXmlAttribute("h-align")
	default void injectHorizontalAlignment(String alignment) {
		AbstractButton comp = injectTargetButtonComponent();
		try {
			Field field = AbstractButton.class.getField(alignment.toUpperCase());
			comp.setHorizontalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
		}
	}

	@InjectXmlAttribute("v-align")
	default void injectVerticalAlignment(String alignment) {
		AbstractButton comp = injectTargetButtonComponent();
		try {
			Field field = AbstractButton.class.getField(alignment.toUpperCase());
			comp.setVerticalAlignment(field.getInt(null));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Invalid vertical alignment: " + alignment, e);
		}
	}

	@InjectXmlAttribute("label")
	default void injectLabel(String label) {
		injectTargetButtonComponent().setText(label);
	}

	@InjectXmlAttribute("selected")
	default void injectSelected(String isSelected) {
		injectTargetButtonComponent().setSelected(Boolean.parseBoolean(isSelected));
	}

	@InjectXmlAttribute("value")
	default void injectValue(String value) {
		injectTargetButtonComponent().setActionCommand(value);
	}

}
