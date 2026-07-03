package sn.tools.swing.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

public interface ComponentUtils {

	public static BufferedImage panelToImage(JPanel panel) {
		Dimension size = panel.getPreferredSize();
		BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		panel.setSize(size);
		panel.doLayout();
		panel.printAll(g2);
		g2.dispose();
		return img;
	}

	public static Icon createScaledIcon(Image img, int w, int h) {
		BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = scaled.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, w, h, null);
		g2.dispose();
		return new ImageIcon(scaled);
	}

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
