package sn.tools.swing.util;

import java.util.function.Consumer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import sn.tools.swing.adapter.PopupTriggerMouseAdapter;
import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

public interface WindowUtils {

    public static final String POPUPMENU_ACTION_MAP_KEY = "showPopup";

    public static JFrame getFrame(ActionEvent event) {
        return getWindow(event, JFrame.class);
    }

    public static JDialog getDialog(ActionEvent event) {
        return getWindow(event, JDialog.class);
    }

    public static Window getWindow(ActionEvent event) {
        Object source = event.getSource();
        if (!(source instanceof Component)) {
            return null;
        }
        Component sourceComp = (Component) source;
        if (sourceComp instanceof JMenuItem) {
            do {
                Component parent = sourceComp.getParent();
                if (parent instanceof JPopupMenu popup) {
                    sourceComp = popup.getInvoker();
                } else {
                    sourceComp = parent;
                }
            } while (sourceComp instanceof JMenuItem);
        }
        return SwingUtilities.getWindowAncestor(sourceComp);
    }

    public static <T extends Window> T getWindow(ActionEvent event, Class<T> windowClazz) {
        Window window = getWindow(event);
        if (window == null) {
            return null;
        }
        return windowClazz.isInstance(window) ? windowClazz.cast(window) : null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Window> T getWindow(ActionEvent event, String windowClazzName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(windowClazzName, true, Thread.currentThread().getContextClassLoader());
            if (Window.class.isAssignableFrom(clazz)) {
                return getWindow(event, (Class<T>) clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void disposeAllWindow(ActionEvent event) {
        for (Window w : Window.getWindows()) {
            if (w.isShowing()) {
                SwingUtilities.invokeLater(() -> w.setVisible(false));
            }
            w.dispose();
        }
    }

    public static void systemExit(ActionEvent event) {
        disposeAllWindow(event);
        System.exit(0);
    }

    public static void moveCenter(ActionEvent event) {
        Window window = getWindow(event);
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        window.setLocation(bounds.x + (bounds.width - window.getWidth()) / 2, (bounds.height - window.getHeight()) / 2);
    }

    public static void moveTopLeft(ActionEvent event) {
        Window window = getWindow(event);
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        window.setLocation(bounds.x, bounds.y);
    }

    public static void moveBottomLeft(ActionEvent event) {
        Window window = getWindow(event);
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        window.setLocation(bounds.x, bounds.height - window.getHeight());
    }

    public static void moveTopRight(ActionEvent event) {
        Window window = getWindow(event);
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        window.setLocation(bounds.x + bounds.width - window.getWidth(), bounds.y);
    }

    public static void moveBottomRight(ActionEvent event) {
        Window window = getWindow(event);
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        window.setLocation(bounds.x + bounds.width - window.getWidth(), bounds.height - window.getHeight());
    }

    public static void applyAllComponent(Container container, Consumer<Component> consumer) {
        consumer.accept(container);
        for (Component c : container.getComponents()) {
            if (c instanceof Container childContainer) {
                applyAllComponent(childContainer, consumer);
            } else {
                consumer.accept(c);
            }
        }
    }

    public static Dimension getScreenRatioSize(double ratio) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) (screen.width * ratio), (int) (screen.height * ratio));
    }

    public static void setPopupMenu(JRootPane rootPane, JPopupMenu popupMenu) {
        setPopupMenuOnMenuKey(rootPane, popupMenu);
        MouseListener listener = new PopupTriggerMouseAdapter(popupMenu);
        Window window = SwingUtilities.getWindowAncestor(rootPane);
        Consumer<Component> consumer = comp -> comp.addMouseListener(listener);
        if (window instanceof JFrame frame) {
            applyAllComponent(frame.getContentPane(), consumer);
        } else if (window instanceof JDialog dialog) {
            applyAllComponent(dialog.getContentPane(), consumer);
        } else {
            applyAllComponent(window, consumer);
        }
    }

    public static void setPopupMenuOnMenuKey(JRootPane rootPane, JPopupMenu popupMenu) {
        ActionListener kl = _ -> {
            if (popupMenu.isVisible()) {
                popupMenu.setVisible(false);
            } else {
                popupMenu.show(rootPane, rootPane.getX(), rootPane.getY());
            }
        };
        KeyUtils.setAction(rootPane, POPUPMENU_ACTION_MAP_KEY, kl);
        KeyUtils.setKey(rootPane, POPUPMENU_ACTION_MAP_KEY, FocusTargetCondition.WINDOW, KeyEvent.VK_F10,
                false, KeyModifiers.SHIFT);
        KeyUtils.setKey(rootPane, POPUPMENU_ACTION_MAP_KEY, FocusTargetCondition.WINDOW, KeyEvent.VK_CONTEXT_MENU,
        		false, KeyModifiers.NONE);
    }

    public static void removePopupMenu(JRootPane rootPane, JPopupMenu popupMenu) {
        KeyUtils.removeKey(rootPane, FocusTargetCondition.WINDOW, KeyEvent.VK_F10, false, KeyModifiers.SHIFT);
        KeyUtils.removeKey(rootPane, FocusTargetCondition.WINDOW, KeyEvent.VK_CONTEXT_MENU, false, KeyModifiers.NONE);
        KeyUtils.removeAction(rootPane, POPUPMENU_ACTION_MAP_KEY);
        Window window = SwingUtilities.getWindowAncestor(rootPane);
        Consumer<Component> consumer = comp -> {
            for (MouseListener ml : comp.getMouseListeners()) {
                if (ml instanceof PopupTriggerMouseAdapter) {
                    comp.removeMouseListener(ml);
                }
            }
        };
        if (window instanceof JFrame frame) {
            applyAllComponent(frame.getContentPane(), consumer);
        } else if (window instanceof JDialog dialog) {
            applyAllComponent(dialog.getContentPane(), consumer);
        } else {
            applyAllComponent(window, consumer);
        }
    }

}
