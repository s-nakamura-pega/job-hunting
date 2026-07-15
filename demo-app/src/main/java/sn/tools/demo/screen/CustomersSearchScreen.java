package sn.tools.demo.screen;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JTextField;

import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.expansion.screen.XmlScreenCreator;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;

@Screen("customers_search")
public class CustomersSearchScreen extends XmlScreenCreator {

	@InjectComponent("name")
	public JTextField name;

	@InjectComponent("address")
	public JTextField address;

	@InjectComponent("tel")
	public JTextField tel;

	@InjectAction("search")
	public void form(ActionEvent event) {
		SimpleParameter sp = new SimpleParameter();
		sp.addParam("name", name.getText());
		sp.addParam("address", address.getText());
		sp.addParam("tel", tel.getText());
		FlowScreenFrame.flow(event, "customers_list", sp);
	}

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/panel/customers_search.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("panel customers_search.xml onInit");
	}

}
