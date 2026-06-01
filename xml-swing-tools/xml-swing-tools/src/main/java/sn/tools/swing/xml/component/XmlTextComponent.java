package sn.tools.swing.xml.component;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import sn.tools.swing.util.ComponentUtils;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;

public interface XmlTextComponent extends XmlComponent {

	/**
	 * DIを行うコンポーネント
	 * 
	 * @return DIを行うコンポーネント
	 */
	JTextComponent injectTargetTextComponent();

	@Override
	default JComponent injectTargetComponent() {
		return injectTargetTextComponent();
	}

	@InjectXmlTextContent
	default void injectValue(String value) {
		injectTargetTextComponent().setText(value);
	}

	@InjectXmlAttribute("undoable")
	default void injectUndoable(String value) {
		if (Boolean.parseBoolean(value)) {
			UndoManager undoManager = ComponentUtils.setUndo(injectTargetTextComponent());
			SwingUtilities.invokeLater(undoManager::discardAllEdits);
		}
	}

}
