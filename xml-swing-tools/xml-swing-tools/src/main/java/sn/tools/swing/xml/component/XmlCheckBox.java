package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

public class XmlCheckBox extends JCheckBox implements XmlButtonComponent {

    private static final long serialVersionUID = 1L;

	@Override
    public AbstractButton injectTargetButtonComponent() {
        return this;
    }

}
