package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

public class XmlCheckBox extends JCheckBox implements XmlButtonComponent {

    @Override
    public AbstractButton injectTargetButtonComponent() {
        return this;
    }

}
