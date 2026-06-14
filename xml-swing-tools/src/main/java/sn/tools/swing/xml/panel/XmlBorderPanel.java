package sn.tools.swing.xml.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlBorderPanel extends XmlPanel {

	private final BorderLayout layout = new BorderLayout();
	private final JPanel panel = new JPanel(layout);

	public XmlBorderPanel() {
	}

	@Override
	public JPanel injectTargetPanel() {
		return panel;
	}

	@InjectXmlAttribute("hgap")
	public void injectHGap(String gap) {
		layout.setHgap(Integer.parseInt(gap));
	}

	@InjectXmlAttribute("vgap")
	public void injectVGap(String gap) {
		layout.setVgap(Integer.parseInt(gap));
	}

	// TODO 各パネル格納

}
