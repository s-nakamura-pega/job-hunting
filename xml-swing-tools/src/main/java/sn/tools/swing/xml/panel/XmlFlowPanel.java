package sn.tools.swing.xml.panel;

import java.awt.FlowLayout;
import javax.swing.JPanel;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public class XmlFlowPanel extends XmlPanel {

	private final FlowLayout layout = new FlowLayout();
	private final JPanel panel = new JPanel(layout);

	@Override
	public JPanel injectTargetPanel() {
		return panel;
	}

	@InjectXmlAttribute("align")
	public void injectAlignment(String align) {
		switch (align.toLowerCase()) {
		case "left" -> layout.setAlignment(FlowLayout.LEFT);
		case "center" -> layout.setAlignment(FlowLayout.CENTER);
		case "right" -> layout.setAlignment(FlowLayout.RIGHT);
		case "leading" -> layout.setAlignment(FlowLayout.LEADING);
		case "trailing" -> layout.setAlignment(FlowLayout.TRAILING);
		default -> throw new IllegalArgumentException("Unknown align: " + align);
		}
	}

	@InjectXmlAttribute("hgap")
	public void injectHGap(String gap) {
		layout.setHgap(Integer.parseInt(gap));
	}

	@InjectXmlAttribute("vgap")
	public void injectVGap(String gap) {
		layout.setVgap(Integer.parseInt(gap));
	}

	@InjectXmlAttribute("baseline")
	public void injectBaseline(String value) {
		layout.setAlignOnBaseline(Boolean.parseBoolean(value));
	}

}
