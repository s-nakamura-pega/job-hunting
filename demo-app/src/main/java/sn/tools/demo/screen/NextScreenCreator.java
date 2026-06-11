package sn.tools.demo.screen;

import java.net.URL;

import javax.swing.JLabel;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.xml.annotation.InjectComponent;
import sn.tools.swing.xml.screen.XmlScreenCreator;

@Screen("next")
public class NextScreenCreator extends XmlScreenCreator {

	@InjectComponent("label")
	public JLabel label;

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/next.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("next.xml onInit");
	}

	@Override
	public void setScreenParameter(ScreenParameter parameter) {
		label.setText(label.getText() + parameter.getParam("text"));
	}

}
