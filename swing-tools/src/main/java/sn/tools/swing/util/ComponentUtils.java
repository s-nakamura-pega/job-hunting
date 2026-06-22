package sn.tools.swing.util;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

public interface ComponentUtils {

	public static void operationUI(JComponent component, Runnable runnable) {
		if (component.isDisplayable()) {
			SwingUtilities.invokeLater(runnable);
		} else {
			runnable.run();
		}
	}

	public static UndoManager setUndo(JTextComponent text) {
		UndoManager undoManager = new UndoManager();
		text.putClientProperty("undoManager", undoManager);
		text.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		ActionListener undo = _ -> {
			if (undoManager.canUndo()) {
				undoManager.undo();
			}
		};
		KeyUtils.setKeyAndAction(text, "Undo", undo, FocusTargetCondition.COMPONENT, KeyEvent.VK_Z, KeyModifiers.CTRL);
		ActionListener redo = _ -> {
			if (undoManager.canRedo()) {
				undoManager.redo();
			}
		};
		KeyUtils.setKeyAndAction(text, "Redo", redo, FocusTargetCondition.COMPONENT, KeyEvent.VK_Y, KeyModifiers.CTRL);
		return undoManager;
	}

}
