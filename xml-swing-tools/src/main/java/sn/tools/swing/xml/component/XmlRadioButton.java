package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JRadioButton;

public class XmlRadioButton extends XmlButtonComponent {

	private static final long serialVersionUID = 1L;

	private final JRadioButton component = new JRadioButton();

	@Override
	public AbstractButton injectTargetButtonComponent() {
		return component;
	}

}
