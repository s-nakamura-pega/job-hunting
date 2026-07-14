package sn.tools.demo.menu;

import java.awt.event.ActionEvent;
import java.net.URL;

import sn.tools.swing.flow.annotation.MenuBar;
import sn.tools.swing.flow.expansion.menu.XmlMenuBarCreator;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.xml.annotation.InjectAction;

@MenuBar("init")
public class DefaultMenuBar extends XmlMenuBarCreator {

	@InjectAction("menu-screen")
	public void flowMenu(ActionEvent event) {
		FlowScreenFrame.flowMenu(event);
	}

	@InjectAction("init-screen")
	public void flowInit(ActionEvent event) {
		SimpleScreenParameter sp = new SimpleScreenParameter();
		FlowScreenFrame.flow(event, "init", sp);
	}

	@InjectAction("next-screen")
	public void flowNext(ActionEvent event) {
		SimpleScreenParameter sp = new SimpleScreenParameter();
		FlowScreenFrame.flow(event, "next", sp);
	}

	@InjectAction("customers_search-screen")
	public void flowCustomersSearchScreen(ActionEvent event) {
		SimpleScreenParameter sp = new SimpleScreenParameter();
		FlowScreenFrame.flow(event, "customers_search", sp);
	}

	@InjectAction("customer_registration-screen")
	public void flowCustomerRegistrationScreen(ActionEvent event) {
		SimpleScreenParameter sp = new SimpleScreenParameter();
		FlowScreenFrame.flow(event, "customer_registration", sp);
	}

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/menu/init.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("menu init.xml onInit");
	}

}
