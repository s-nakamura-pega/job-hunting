package sn.tools.demo.screen;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JTextField;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.util.WindowUtils;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;
import sn.tools.swing.xml.screen.XmlScreenCreator;

@Screen("init")
public class InitScreenCreator extends XmlScreenCreator {

	@InjectComponent("name")
	public JTextField text;

	@InjectAction("form")
	public void form(ActionEvent event) {
		ScreenParameter sp = new SimpleScreenParameter();
		sp.addParam("text", text.getText());
		FlowScreenFrame.flow(event, "next", sp);
	}
	
	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/init.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("init.xml onInit");
	}

	@Override
	public void setScreenParameter(ScreenParameter parameter) {
	}

}
