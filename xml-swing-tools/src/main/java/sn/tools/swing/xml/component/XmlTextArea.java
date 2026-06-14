package sn.tools.swing.xml.component;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class XmlTextArea extends XmlTextComponent {

	private final JTextArea component = new JTextArea();

	@Override
	public JTextComponent injectTargetTextComponent() {
		return component;
	}

}
