package sn.tools.swing.xml.component;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.w3c.dom.Element;

import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public class XmlSplitPane extends XmlComponent {

	private final JSplitPane splitPane = new JSplitPane();
	private Map<String, XmlComponent> componentMap;

	@Override
	public void setComponentMap(Map<String, XmlComponent> componentMap) {
		this.componentMap = componentMap;
	}

	// orientation="horizontal" or "vertical"
	@InjectXmlAttribute("orientation")
	public void setOrientation(String value) {
		if ("vertical".equalsIgnoreCase(value)) {
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		} else {
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		}
	}

	// divider="200"
	@InjectXmlAttribute("divider")
	public void setDividerLocation(String value) {
		try {
			splitPane.setDividerLocation(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			// 無視（デフォルトのまま）
		}
	}

	// resize-weight="0.0〜1.0"
	@InjectXmlAttribute("resize-weight")
	public void setResizeWeight(String value) {
		try {
			splitPane.setResizeWeight(Double.parseDouble(value));
		} catch (NumberFormatException e) {
			// 無視
		}
	}

	// continuous="true|false"
	@InjectXmlAttribute("continuous")
	public void setContinuousLayout(String value) {
		splitPane.setContinuousLayout(Boolean.parseBoolean(value));
	}

	// 子要素（2つまで）
	@InjectXmlElement(".+")
	public void injectChild(Element element) {
		new XmlObjectCreator<>(element, Child.class).addConstructorArgument(XmlSplitPane.class, this).create();
	}

	@Override
	public JComponent injectTargetComponent() {
		return splitPane;
	}

	// -------------------------
	// 子要素を処理する内部クラス
	// -------------------------
	public class Child {

		private int index = -1;

		public Child(XmlSplitPane parent) {
			if (parent.splitPane.getLeftComponent() == null) {
				index = 0;
			} else if (parent.splitPane.getRightComponent() == null) {
				index = 1;
			} else {
				throw new IllegalStateException("XmlSplitPane can have only 2 child components.");
			}
		}

		@InjectXmlElement(".+")
		public void set(Element element) {
			CreateUtils.createXmlComponentOrXmlPanelAndPutcomponentMap(element, componentMap).ifPresent(comp -> {
				if (index == 0) {
					splitPane.setLeftComponent(comp.injectTargetComponent());
				} else {
					splitPane.setRightComponent(comp.injectTargetComponent());
				}
			});
		}
	}

}
