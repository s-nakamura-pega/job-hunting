package sn.tools.demo.screen;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.expansion.screen.XmlScreenCreator;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;

@Screen("next")
public class NextScreenCreator extends XmlScreenCreator {

	@InjectComponent("label")
	public JLabel label;

	@InjectAction("back")
	public void back(ActionEvent event) {
		FlowScreenFrame.flow(event, "init", new SimpleScreenParameter());
	}

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/panel/next.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("panel next.xml onInit");
	}

	@Override
	public void onEnter(ScreenParameter parameter) {
		SwingUtilities
				.invokeLater(() -> parameter.getParam("text", String.class).ifPresent(text -> label.setText(text)));
	}

	@Override
	public boolean isDisplayCatalog() {
		return false;
	}

}
