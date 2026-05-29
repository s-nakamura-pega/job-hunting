package sn.tools.swing.xml.component;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.xml.bind.annotation.XmlObject;
import sn.tools.xml.bind.creator.XmlObjectCreator;
import sn.tools.xml.dom.DocumentUtils;

@XmlObject("text-field")
public class XmlTextField extends JTextField implements XmlComponent {
	
}
