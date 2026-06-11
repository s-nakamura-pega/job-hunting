package sn.tools.swing.xml.panel;

import java.awt.FlowLayout;
import javax.swing.JPanel;

public class XmlFlowPanel extends XmlPanel {

	private static final long serialVersionUID = 1L;
	
	private final JPanel panel = new JPanel();

	public XmlFlowPanel() {
		panel.setLayout(new FlowLayout());
	}

	@Override
	public JPanel injectTargetPanel() {
		return panel;
	}

}
