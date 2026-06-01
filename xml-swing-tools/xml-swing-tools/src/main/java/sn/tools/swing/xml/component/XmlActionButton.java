package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JButton;

public class XmlActionButton extends JButton implements XmlButtonComponent {

    @Override
    public AbstractButton injectTargetButtonComponent() {
        return this;
    }

}
