package sn.tools.swing.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

public interface KeyUtils {

    public static void setKeyAndAction(JComponent component, Object actionMapKey, ActionListener action,
            FocusTargetCondition targetCondition, int keyCode, KeyModifiers... modifiers) {
        setKeyAndAction(component, actionMapKey, action, targetCondition, keyCode, modifiers);
    }

    public static void setKey(JComponent component, Object actionMapKey, FocusTargetCondition targetCondition,
            int keyCode, KeyModifiers... modifiers) {
    	KeyStroke ks = KeyStroke.getKeyStroke(keyCode, KeyModifiers.of(modifiers));
        targetCondition.getTargetList().forEach(cond -> component.getInputMap(cond).put(ks, actionMapKey));
    }

    public static void setAction(JComponent component, Object actionMapKey, ActionListener action) {
        component.getActionMap().put(actionMapKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }

    public static void removeKeyAndAction(JComponent component, Object actionMapKey,
            FocusTargetCondition targetCondition, int keyCode, KeyModifiers... modifiers) {
        removeKey(component, targetCondition, keyCode, modifiers);
        removeAction(component, actionMapKey);
    }

    public static void removeKey(JComponent component, FocusTargetCondition targetCondition, int keyCode,
            KeyModifiers... modifiers) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, KeyModifiers.of(modifiers));
        targetCondition.getTargetList().forEach(cond -> component.getInputMap(cond).remove(ks));
    }

    public static void removeAction(JComponent component, Object actionMapKey) {
        Objects.requireNonNull(actionMapKey, "actionMapKey must not be null");
        component.getActionMap().remove(actionMapKey);
    }

}
