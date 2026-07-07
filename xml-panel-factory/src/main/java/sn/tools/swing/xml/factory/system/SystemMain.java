package sn.tools.swing.xml.factory.system;

import javax.swing.SwingUtilities;

import sn.tools.swing.xml.factory.frame.XmlPanelFactoryFrame;

public class SystemMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new XmlPanelFactoryFrame().setVisible(true));
	}

}
