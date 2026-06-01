package sn.tools.swing.adapter;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class PopupTriggerMouseAdapter extends MouseAdapter {
    private final JPopupMenu popupMenu;

    public PopupTriggerMouseAdapter(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    private void showMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Component component = e.getComponent();
            Point compPoint = component.getLocationOnScreen();
            Window window = SwingUtilities.getWindowAncestor(e.getComponent());
            int x = (window.getX() + window.getWidth() < compPoint.x + e.getX() + popupMenu.getWidth())
                    ? e.getX() - popupMenu.getWidth()
                    : e.getX();
            int y = (window.getY() + window.getHeight() < compPoint.y + e.getY() + popupMenu.getHeight())
                    ? e.getY() - popupMenu.getHeight()
                    : e.getY();
            popupMenu.show(component, x, y);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        showMenu(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showMenu(e);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PopupTriggerMouseAdapter ptma) {
            return ptma.popupMenu == this.popupMenu;
        }
        return false;
    }
}
