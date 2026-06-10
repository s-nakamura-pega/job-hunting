package sn.tools.demo.screen;

import java.net.URL;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.xml.screen.XmlScreenCreator;

@Screen("next")
public class NextScreenCreator extends XmlScreenCreator {

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/next.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("next.xml onInit");
	}

}
