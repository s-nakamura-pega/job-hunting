package sn.tools.swing.xml.panel;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class XmlFlowPanel extends JPanel implements XmlPanel {

	private static final long serialVersionUID = 1L;

	public XmlFlowPanel() {
		super();
		setLayout(new FlowLayout());
	}

	@Override
	public JPanel injectTargetPanel() {
		return this;
	}

}
