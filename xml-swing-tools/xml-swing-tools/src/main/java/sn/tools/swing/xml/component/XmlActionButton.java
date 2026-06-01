package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JButton;

public class XmlActionButton extends JButton implements XmlButtonComponent {

    private static final long serialVersionUID = 1L;

	@Override
    public AbstractButton injectTargetButtonComponent() {
        return this;
    }

}
