package sn.tools.swing.xml.panel;

import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.JPanel;

import sn.tools.swing.xml.component.XmlComponent;

public class XmlFlowPanel extends XmlPanel {

	private static final long serialVersionUID = 1L;
	
	private final JPanel panel = new JPanel();

	public XmlFlowPanel(Map<String, XmlComponent> componentMap) {
		super(componentMap);
		panel.setLayout(new FlowLayout());
	}

	@Override
	public JPanel injectTargetPanel() {
		return panel;
	}

}
