package sn.tools.swing.xml.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.JComponent;

import sn.tools.swing.xml.parts.XmlParts;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public abstract class XmlComponent implements XmlParts {

	private String id;

	@Override
	public String getId() {
		return id;
	}

	@InjectXmlAttribute("id")
	public void setId(String id) {
		this.id = id;
	}

	public void setComponentMap(Map<String, XmlComponent> componentMap) {
	}

	@InjectXmlAttribute("width")
	public void injectWidth(String width) {
		JComponent comp = injectTargetComponent();
		Dimension preferredSize = comp.getPreferredSize();
		comp.setPreferredSize(new Dimension(Integer.parseInt(width), preferredSize.height));
	}

	@InjectXmlAttribute("height")
	public void injectHeight(String height) {
		JComponent comp = injectTargetComponent();
		Dimension preferredSize = comp.getPreferredSize();
		comp.setPreferredSize(new Dimension(preferredSize.width, Integer.parseInt(height)));
	}

	@InjectXmlAttribute("min-width")
	public void injectMinWidth(String width) {
		JComponent comp = injectTargetComponent();
		Dimension minSize = comp.getMinimumSize();
		comp.setMinimumSize(new Dimension(Integer.parseInt(width), minSize.height));
	}

	@InjectXmlAttribute("min-height")
	public void injectMinHeight(String height) {
		JComponent comp = injectTargetComponent();
		Dimension minSize = comp.getMinimumSize();
		comp.setMinimumSize(new Dimension(minSize.width, Integer.parseInt(height)));
	}

	@InjectXmlAttribute("max-width")
	public void injectMaxWidth(String width) {
		JComponent comp = injectTargetComponent();
		Dimension maxSize = comp.getMaximumSize();
		comp.setMaximumSize(new Dimension(Integer.parseInt(width), maxSize.height));
	}

	@InjectXmlAttribute("max-height")
	public void injectMaxHeight(String height) {
		JComponent comp = injectTargetComponent();
		Dimension maxSize = comp.getMaximumSize();
		comp.setMaximumSize(new Dimension(maxSize.width, Integer.parseInt(height)));
	}

	@InjectXmlAttribute("tooltip")
	public void injectTooltip(String tooltip) {
		injectTargetComponent().setToolTipText(tooltip);
	}

	@InjectXmlAttribute("font-size")
	public void injectFontSize(String fontSize) {
		JComponent comp = injectTargetComponent();
		Font font = comp.getFont();
		comp.setFont(new Font(font.getName(), font.getStyle(), Integer.parseInt(fontSize)));
	}

	@InjectXmlAttribute("font-style")
	public void injectFontStyle(String fontStyle) {
		int fontStyleValue = switch (fontStyle.toLowerCase()) {
		case "bold" -> Font.BOLD;
		case "italic" -> Font.ITALIC;
		default -> Font.PLAIN;
		};
		JComponent comp = injectTargetComponent();
		Font font = comp.getFont();
		comp.setFont(new Font(font.getName(), fontStyleValue, font.getSize()));
	}

	@InjectXmlAttribute("font-color")
	public void injectFontColor(String color) {
		injectTargetComponent().setForeground(getColor(color));
	}

	@InjectXmlAttribute("background-color")

	public void injectBackgroundColor(String color) {
		JComponent comp = injectTargetComponent();
		comp.setBackground(getColor(color));
		comp.setOpaque(true);
	}

	private Color getColor(String fontColor) {
		if (fontColor.startsWith("#")) {
			return Color.decode(fontColor);
		}
		if (fontColor.contains(",")) {
			String[] rgb = fontColor.split(",");
			return new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()),
					Integer.parseInt(rgb[2].trim()));
		}
		return switch (fontColor.toLowerCase()) {
		case "red" -> Color.RED;
		case "blue" -> Color.BLUE;
		case "gray" -> Color.GRAY;
		case "lightgray" -> Color.LIGHT_GRAY;
		case "darkgray" -> Color.DARK_GRAY;
		case "green" -> Color.GREEN;
		case "yellow" -> Color.YELLOW;
		case "white" -> Color.WHITE;
		case "black" -> Color.BLACK;
		default -> Color.BLACK;
		};
	}

	/**
	 * DIを行うコンポーネント
	 * 
	 * @return DIを行うコンポーネント
	 */
	public abstract JComponent injectTargetComponent();

}
