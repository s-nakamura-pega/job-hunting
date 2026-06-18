package sn.tools.swing.xml.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.w3c.dom.Element;

import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public class XmlBorderPanel extends XmlPanel {

	private final BorderLayout layout = new BorderLayout();
	private final JPanel panel = new JPanel(layout);

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

	@InjectXmlElement({ "north", "west", "east", "south", "center" })
	public void injectPositionParts(Element element) {
		String position = switch (element.getLocalName()) {
		case "north" -> BorderLayout.NORTH;
		case "west" -> BorderLayout.WEST;
		case "east" -> BorderLayout.EAST;
		case "south" -> BorderLayout.SOUTH;
		case "center" -> BorderLayout.CENTER;
		default -> throw new IllegalArgumentException("Unknown position: " + element.getLocalName());
		};
		new XmlObjectCreator<>(element, PositionParts.class).addConstructorArgument(XmlBorderPanel.class, this)
				.addConstructorArgument(String.class, position).create();
	}

	public class PositionParts {

		private final String position;

		public PositionParts(String position) {
			this.position = position;
		}

		@InjectXmlElement(".+")
		public void set(Element element) {
			CreateUtils.createXmlComponentOrXmlPanelAndPutcomponentMap(element, componentMap)
					.ifPresent(comp -> panel.add(comp.injectTargetComponent(), position));
		}

	}

}
