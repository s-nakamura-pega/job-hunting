package sn.tools.swing.xml.component;

import java.awt.Dimension;
import javax.swing.JComponent;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public interface XmlComponent {

	/**
	 * DIを行うコンポーネント
	 * 
	 * @return DIを行うコンポーネント
	 */
	JComponent injectTargetComponent();

	@InjectXmlAttribute("width")
	default void injectWidth(String width) {
		JComponent comp = injectTargetComponent();
		Dimension preferredSize = comp.getPreferredSize();
		comp.setPreferredSize(new Dimension(Integer.parseInt(width), preferredSize.height));
	}

	@InjectXmlAttribute("height")
	default void injectHeight(String height) {
		JComponent comp = injectTargetComponent();
		Dimension preferredSize = comp.getPreferredSize();
		comp.setPreferredSize(new Dimension(preferredSize.width, Integer.parseInt(height)));
	}

	@InjectXmlAttribute("min-width")
	default void injectMinWidth(String width) {
		JComponent comp = injectTargetComponent();
		Dimension minSize = comp.getMinimumSize();
		comp.setMinimumSize(new Dimension(Integer.parseInt(width), minSize.height));
	}

	@InjectXmlAttribute("max-height")
	default void injectMinHeight(String height) {
		JComponent comp = injectTargetComponent();
		Dimension minSize = comp.getMinimumSize();
		comp.setMinimumSize(new Dimension(minSize.width, Integer.parseInt(height)));
	}

	@InjectXmlAttribute("max-width")
	default void injectMaxWidth(String width) {
		JComponent comp = injectTargetComponent();
		Dimension maxSize = comp.getMaximumSize();
		comp.setMaximumSize(new Dimension(Integer.parseInt(width), maxSize.height));
	}

	@InjectXmlAttribute("min-height")
	default void injectMaxHeight(String height) {
		JComponent comp = injectTargetComponent();
		Dimension maxSize = comp.getMaximumSize();
		comp.setMaximumSize(new Dimension(maxSize.width, Integer.parseInt(height)));
	}

	@InjectXmlAttribute("tooltip")
	default void injectTooltip(String tooltip) {
		injectTargetComponent().setToolTipText(tooltip);
	}

}
