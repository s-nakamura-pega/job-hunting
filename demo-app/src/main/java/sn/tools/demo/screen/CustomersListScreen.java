package sn.tools.demo.screen;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import sn.tools.clazz.creator.SimpleObjectCreator;
import sn.tools.db.execute.QueryExecutor;
import sn.tools.db.execute.Where;
import sn.tools.db.response.DBResponse;
import sn.tools.demo.db.DBManager;
import sn.tools.demo.entity.Customers;
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

		Where where = new Where()
				.appendWithValidate("name like ?", name.filter(v -> !v.isBlank()).isPresent(),
						name.map(v -> "%" + v + "%").orElse(null))
				.appendWithValidate("address like ?", address.filter(v -> !v.isBlank()).isPresent(),
						address.map(v -> "%" + v + "%").orElse(null))
				.appendWithValidate("phone like ?", tel.filter(v -> !v.isBlank()).isPresent(),
						tel.map(v -> "%" + v + "%").orElse(null));

		QueryExecutor sql = DBManager.getQueryExecutor();
		List<DBResponse> responseList = sql.append("SELECT * FROM customers").append(where).append("ORDER BY id ASC")
				.addCreator(new SimpleObjectCreator<>(Customers.class)).execute();

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		if (!responseList.isEmpty()) {
			for (DBResponse res : responseList) {
				Object[] rowData = new Object[4];
				Customers customers = res.get(Customers.class);
				rowData[0] = customers.id;
				rowData[1] = customers.name;
				rowData[2] = customers.address;
				rowData[3] = customers.phone;
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

	@Override
	public boolean isDisplayCatalog() {
		return false;
	}

}
