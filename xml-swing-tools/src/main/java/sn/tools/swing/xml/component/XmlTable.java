package sn.tools.swing.xml.component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Element;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;

public class XmlTable extends XmlComponent {

	private final DefaultTableModel model = new DefaultTableModel();
	private JTable component = new JTable(model);

	@InjectXmlAttribute("editable")
	public void setEditable(String editable) {
		if (!Boolean.getBoolean(editable)) {
			component = new JTable(model) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
	}

	@InjectXmlElement("column")
	public void injectColumn(Element element) {
		model.addColumn(element.getTextContent().trim());
	}

	@InjectXmlElement("row")
	public void injectRow(Element element) {
		var list = element.getChildNodes();
		Object[] row = new Object[list.getLength()];
		int idx = 0;
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element elem) {
				row[idx++] = elem.getTextContent().trim();
			}
		}
		model.addRow(row);
	}

	@Override
	public JComponent injectTargetComponent() {
		return component;
	}
}
