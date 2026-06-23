package sn.tools.demo.screen;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import sn.tools.db.sql.Where;
import sn.tools.demo.db.DBManager;
import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.expansion.screen.XmlScreenCreator;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;
import sn.tools.swing.flow.parameter.SimpleScreenParameter;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;

@Screen("customers_list")
public class CustomersListScreen extends XmlScreenCreator {

	@InjectComponent("list")
	public JTable table;

	@InjectAction("back")
	public void form(ActionEvent event) {
		FlowScreenFrame.flow(event, "customers_search", new SimpleScreenParameter());
	}

	private AtomicBoolean hasData = new AtomicBoolean(false);

	@Override
	public void onEnter(ScreenParameter sp) {
		Optional<String> name = sp.getParam("name", String.class);
		Optional<String> address = sp.getParam("address", String.class);
		Optional<String> tel = sp.getParam("tel", String.class);
		StringBuilder sql = new StringBuilder("SELECT * FROM customers");
		Where where = new Where();
		where.add("name like ?", _ -> name.isEmpty() || name.get().isBlank(), name.map(v -> "%" + v + "%").orElse(null));
		where.add("address like ?", _ -> address.isEmpty() || address.get().isBlank(), address.map(v -> "%" + v + "%").orElse(null));
		where.add("phone like ?", _ -> tel.isEmpty() || tel.get().isBlank(), tel.map(v -> "%" + v + "%").orElse(null));
		sql.append(where);
		sql.append("ORDER BY id ASC");
		System.out.println(sql);
		List<Map<String, Object>> result = DBManager.getDBExecutor().query(sql.toString(), where.getBinds());
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		if (!result.isEmpty()) {
			for (Map<String, Object> rowMap : result) {
				Object[] rowData = new Object[rowMap.size()];
				rowData[0] = rowMap.get("id");
				rowData[1] = rowMap.getOrDefault("name", "");
				rowData[2] = rowMap.getOrDefault("address", "");
				rowData[3] = rowMap.getOrDefault("phone", "");
				model.addRow(rowData);
			}
			hasData.set(true);
		} else {
			hasData.set(false);
		}
	}

	@Override
	public void onDisplay(ScreenParameter parameter) {
		if (!hasData.get()) {
			JOptionPane.showMessageDialog(getCreation(), "条件に一致するデータが見つかりませんでした。");
		}
	}

	@Override
	protected URL xmlURL() {
		return getClass().getClassLoader().getResource("sn/tools/demo/xml/panel/customers_list.xml");
	}

	@Override
	protected void onInit() {
		System.out.println("panel customers_list.xml onInit");
	}

}
