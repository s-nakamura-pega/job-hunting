package sn.tools.swing.xml.menu;

import javax.swing.JMenuItem;

import sn.tools.swing.xml.parts.XmlParts;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public abstract class XmlMenuItemComponent<T extends JMenuItem> implements XmlParts {

	private String id;

	@InjectXmlAttribute("label")
	public void injectLabel(String label) {
		injectTargetComponent().setText(label);
	}

	@Override
	public String getId() {
		return id;
	}

	@InjectXmlAttribute("id")
	public void setId(String id) {
		this.id = id;
	}

	public abstract T injectTargetComponent();

}
