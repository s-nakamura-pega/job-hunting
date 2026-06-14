package sn.tools.swing.xml.menu;

import java.util.Map;

import javax.swing.JMenuBar;

import org.w3c.dom.Element;

import sn.tools.swing.xml.parts.XmlParts;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public class XmlMenuBar implements XmlParts {

	private final JMenuBar menuBar = new JMenuBar();
	private String id;

	private Map<String, XmlMenuItemComponent<?>> componentMap;

	public void setComponentMap(Map<String, XmlMenuItemComponent<?>> componentMap) {
		this.componentMap = componentMap;
	}

	@InjectXmlElement("menu")
	public void setMenu(Element element) {
		XmlObjectCreator<XmlMenu> creator = new XmlObjectCreator<>(element, XmlMenu.class);
		creator.setPreDecorateProcess(menu -> menu.setComponentMap(componentMap));
		XmlMenu menu = creator.create();
		String id = menu.getId();
		if (id != null) {
			componentMap.put(id, menu);
		}
		menuBar.add(menu.injectTargetComponent());
	}

	@Override
	public String getId() {
		return id;
	}

	@InjectXmlAttribute("id")
	public void setId(String id) {
		this.id = id;
	}

	public JMenuBar injectTargetMenuBar() {
		return menuBar;
	}

}
