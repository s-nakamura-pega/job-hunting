package sn.tools.swing.xml.screen;

import java.net.URL;
import java.util.Optional;

import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.swing.xml.panel.XmlPanelConfigs;
import sn.tools.xml.dom.DocumentUtils;
import sn.tools.xml.dom.DomElementWrapper;

public abstract class ScreenCreator implements XmlPanelConfigs {

	public JPanel create() {
		Document doc = DocumentUtils.read(xmlURL(), false);
		doc.getDocumentElement().normalize();
		DomElementWrapper dew = new DomElementWrapper(doc.getDocumentElement());
		Optional<Element> elementOpt = dew.childElementStream().findFirst();
		if (elementOpt.isEmpty()) {
			return new JPanel();
		}
		Element element = elementOpt.get();
		return PANEL_CONFIGS.get(element.getTagName()).getPanel(element).injectTargetPanel();
	}

	abstract protected URL xmlURL();

}
