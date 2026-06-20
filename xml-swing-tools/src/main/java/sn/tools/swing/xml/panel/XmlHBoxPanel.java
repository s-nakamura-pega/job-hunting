package sn.tools.swing.xml.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlHBoxPanel extends XmlPanel {

	private final JPanel panel = new JPanel();

	public XmlHBoxPanel() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	}

	@Override
	public JPanel injectTargetPanel() {
		return panel;
	}

	@InjectXmlAttribute("align-x")
	public void injectAlignX(String value) {
		panel.setAlignmentX(Float.parseFloat(value));
	}

	@InjectXmlAttribute("align-y")
	public void injectAlignY(String value) {
		panel.setAlignmentY(Float.parseFloat(value));
	}

}
