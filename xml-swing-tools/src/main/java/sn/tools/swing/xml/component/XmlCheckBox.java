package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

public class XmlCheckBox extends XmlButtonComponent {

	private static final long serialVersionUID = 1L;

	private final JCheckBox component = new JCheckBox();

	@Override
	public AbstractButton injectTargetButtonComponent() {
		return component;
	}

}
