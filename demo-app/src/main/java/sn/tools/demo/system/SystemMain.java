package sn.tools.demo.system;

import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import sn.tools.demo.db.DBManager;
import sn.tools.demo.frame.MainFrame;

public class SystemMain {

	public static void main(String[] args) {
		DBManager.initDB();
		List<Map<String,Object>> customers = DBManager.getDBExecutor().query("select * from customers");
		List<Map<String,Object>> orders = DBManager.getDBExecutor().query("select * from orders");
		List<Map<String,Object>> orderItems = DBManager.getDBExecutor().query("select * from order_items");
		System.out.println("-- customers");
		System.out.println(customers);
		System.out.println();
		System.out.println("-- orders");
		System.out.println(orders);
		System.out.println();
		System.out.println("-- order_items");
		System.out.println(orderItems);
		System.out.println();
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}

}
