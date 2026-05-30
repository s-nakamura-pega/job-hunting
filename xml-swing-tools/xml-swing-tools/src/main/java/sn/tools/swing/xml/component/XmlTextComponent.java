package sn.tools.swing.xml.component;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public interface XmlTextComponent extends XmlComponent {

	/**
	 * DIを行うコンポーネント
	 * 
	 * @return DIを行うコンポーネント
	 */
	JTextComponent injectTargetTextComponent();

	@Override
	default JComponent injectTargetComponent() {
		return injectTargetTextComponent();
	}

	@InjectXmlAttribute("value")
	default void injectValue(String value) {
		injectTargetTextComponent().setText(value);
	}

}
