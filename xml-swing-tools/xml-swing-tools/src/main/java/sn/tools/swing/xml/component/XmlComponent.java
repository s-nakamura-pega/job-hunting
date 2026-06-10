package sn.tools.swing.xml.component;

import java.awt.Dimension;
import javax.swing.JComponent;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public abstract class XmlComponent {

	private String id;

	public String getId() {
		return id;
	}

	@InjectXmlAttribute("id")
	public void setId(String id) {
		this.id = id;
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

	/**
	 * DIを行うコンポーネント
	 * 
	 * @return DIを行うコンポーネント
	 */
	public abstract JComponent injectTargetComponent();

}
