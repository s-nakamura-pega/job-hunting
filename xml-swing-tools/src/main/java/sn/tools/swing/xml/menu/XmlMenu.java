package sn.tools.swing.xml.menu;

import java.util.Map;

import javax.swing.JMenu;

import org.w3c.dom.Element;

import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public class XmlMenu extends XmlMenuItemComponent<JMenu> {

	private final JMenu component = new JMenu();

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
		component.add(menu.injectTargetComponent());
	}

	@InjectXmlElement("item")
	public void setMenuItem(Element element) {
		XmlMenuItem item = new XmlObjectCreator<>(element, XmlMenuItem.class).create();
		String id = item.getId();
		if (id != null) {
			componentMap.put(id, item);
		}
		component.add(item.injectTargetComponent());
	}

	@Override
	public JMenu injectTargetComponent() {
		return component;
	}

}
