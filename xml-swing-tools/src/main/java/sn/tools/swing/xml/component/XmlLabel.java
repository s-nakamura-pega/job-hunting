package sn.tools.swing.xml.component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;

public class XmlLabel extends XmlComponent {

	private final JLabel component = new JLabel();

	@InjectXmlAttribute("h-align")
	public void injectHorizontalAlignment(String alignment) {
		int align = switch (alignment.toLowerCase()) {
		case "left" -> SwingConstants.LEFT;
		case "center" -> SwingConstants.CENTER;
		case "right" -> SwingConstants.RIGHT;
		case "leading" -> SwingConstants.LEADING;
		case "trailing" -> SwingConstants.TRAILING;
		default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
		};
		component.setHorizontalAlignment(align);
	}

	@InjectXmlAttribute("v-align")
	public void injectVerticalAlignment(String alignment) {
		int align = switch (alignment.toLowerCase()) {
		case "top" -> SwingConstants.TOP;
		case "center" -> SwingConstants.CENTER;
		case "bottom" -> SwingConstants.BOTTOM;
		default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
		};
		component.setVerticalAlignment(align);
	}

	@InjectXmlTextContent
	public void injectLabel(String label) {
		component.setText(label);
	}

	@Override
	public JComponent injectTargetComponent() {
		return component;
	}

}
