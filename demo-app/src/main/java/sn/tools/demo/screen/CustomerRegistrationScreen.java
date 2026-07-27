package sn.tools.demo.screen;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import sn.tools.demo.db.DBManager;
import sn.tools.demo.entity.Customers;
import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.expansion.screen.XmlScreenCreator;

import sn.tools.swing.util.WindowUtils;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;

@Screen("customer_registration")
public class CustomerRegistrationScreen extends XmlScreenCreator {

	@InjectComponent("name")
	public JTextField name;

	@InjectComponent("address")
	public JTextField address;

	@InjectComponent("tel")
	public JTextField tel;

	@InjectAction("register")
	public void form(ActionEvent event) {
		Window window = WindowUtils.getWindow(event);
		String nameText =name.getText(); 
		if (nameText.isBlank()) {			
			JOptionPane.showMessageDialog(window, "Customer is not entered.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Customers customers = new Customers();
		customers.name = nameText;
		customers.address = address.getText();
		customers.phone = tel.getText();
		customers.createdAt = new Date(new java.util.Date().getTime()).toString();
		try {
			DBManager.getDBExecutor().insert(customers);
			JOptionPane.showMessageDialog(window, "Registration completed.", "Successful", JOptionPane.INFORMATION_MESSAGE);
			name.setText(null);
			address.setText(null);
			tel.setText(null);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(window, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/panel/customer_registration.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("panel customer_registration.xml onInit");
	}

}
