package sn.tools.swing.xml.panel;

import java.awt.GridLayout;

import javax.swing.JPanel;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlGridPanel extends XmlPanel {

	private final GridLayout layout = new GridLayout();
	private final JPanel panel = new JPanel(layout);

	@Override
	public JPanel injectTargetPanel() {
		return panel;
	}

	@InjectXmlAttribute("rows")
	public void injectRows(String rows) {
		layout.setRows(Integer.parseInt(rows));
	}

	@InjectXmlAttribute("cols")
	public void injectCols(String cols) {
		layout.setColumns(Integer.parseInt(cols));
	}

	@InjectXmlAttribute("hgap")
	public void injectHGap(String gap) {
		layout.setHgap(Integer.parseInt(gap));
	}

	@InjectXmlAttribute("vgap")
	public void injectVGap(String gap) {
		layout.setVgap(Integer.parseInt(gap));
	}

}
