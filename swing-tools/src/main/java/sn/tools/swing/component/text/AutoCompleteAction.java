package sn.tools.swing.component.text;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AutoCompleteAction implements ActionListener {

	private final JTextArea area;
	private final List<String> candidates;

	public AutoCompleteAction(JTextArea area, List<String> candidates) {
		this.area = area;
		this.candidates = candidates;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			int pos = area.getCaretPosition();
			Document doc = area.getDocument();
			String text = doc.getText(0, doc.getLength());

			int start = text.lastIndexOf('<', pos - 1);
			if (start < 0)
				return;

			String prefix = text.substring(start, pos);

			for (String cand : candidates) {
				if (cand.startsWith(prefix)) {
					String completion = cand.substring(prefix.length());
					doc.insertString(pos, completion, null);
					return;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
