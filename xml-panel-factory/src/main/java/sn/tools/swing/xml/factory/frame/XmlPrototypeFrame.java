package sn.tools.swing.xml.factory.frame;

import java.util.HashMap;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.swing.util.WindowUtils;
import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.swing.xml.panel.XmlPanel;
import sn.tools.xml.dom.DocumentUtils;
import sn.tools.xml.dom.DomElementWrapper;

public class XmlPrototypeFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public XmlPrototypeFrame() {
		super();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize(WindowUtils.getScreenRatioSize(0.7));
		setLocationRelativeTo(null);
	}

	public void setPanel(String xml) {
		Document doc = DocumentUtils.read(xml, true);
		doc.getDocumentElement().normalize();
		DomElementWrapper dew = new DomElementWrapper(doc.getDocumentElement());
		Optional<Element> elementOpt = dew.childElementStream().findFirst();
		elementOpt.ifPresent(_ -> {
			XmlPanel xmlPanel = CreateUtils.createXmlPanelAndPutcomponentMap(elementOpt.get(), new HashMap<String, XmlComponent>());
			changeScreen(xmlPanel.injectTargetPanel());
		});
	}

	private void changeScreen(JPanel panel) {
		SwingUtilities.invokeLater(() -> {
			setContentPane(panel);
			revalidate();
			repaint();
		});
	}

}
