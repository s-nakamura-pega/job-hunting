package sn.tools.swing.xml.component;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.w3c.dom.Element;

import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public class XmlTabs extends XmlComponent {

	private final JTabbedPane tabs = new JTabbedPane();
	private Map<String, XmlComponent> componentMap;

	@Override
	public void setComponentMap(Map<String, XmlComponent> componentMap) {
		this.componentMap = componentMap;
	}

	@InjectXmlElement("tab")
	public void injectPositionParts(Element element) {
		new XmlObjectCreator<>(element, Tab.class).addConstructorArgument(XmlTabs.class, this).create();
	}

	@Override
	public JComponent injectTargetComponent() {
		return tabs;
	}

	public class Tab {

		private String title;

		@InjectXmlAttribute("title")
		public void setTitle(String title) {
			this.title = title;
		}

		@InjectXmlElement(".+")
		public void set(Element element) {
			CreateUtils.createXmlComponentOrXmlPanelAndPutcomponentMap(element, componentMap)
					.ifPresent(comp -> tabs.add(title, comp.injectTargetComponent()));
		}

	}

}
