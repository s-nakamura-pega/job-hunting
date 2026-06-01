package sn.tools.swing.xml.component;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import sn.tools.swing.util.KeyUtils;
import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;
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
			setUndo(injectTargetTextComponent());
		}
	}

	public static void setUndo(JTextComponent text) {
		UndoManager undoManager = new UndoManager();
		text.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		ActionListener undo = event -> {
			if (undoManager.canUndo()) {
				undoManager.undo();
			}
		};
		KeyUtils.setKeyAndAction(text, "Undo", undo, FocusTargetCondition.COMPONENT, KeyEvent.VK_Z, KeyModifiers.CTRL);
		ActionListener redo = event -> {
			if (undoManager.canRedo()) {
				undoManager.redo();
			}
		};
		KeyUtils.setKeyAndAction(text, "Redo", redo, FocusTargetCondition.COMPONENT, KeyEvent.VK_Y, KeyModifiers.CTRL);
	}

}
