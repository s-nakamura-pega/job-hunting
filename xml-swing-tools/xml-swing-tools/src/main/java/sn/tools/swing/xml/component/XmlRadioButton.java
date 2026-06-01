package sn.tools.swing.xml.component;

import javax.swing.AbstractButton;
import javax.swing.JRadioButton;

public class XmlRadioButton extends JRadioButton implements XmlButtonComponent {

    @Override
    public AbstractButton injectTargetButtonComponent() {
        return this;
    }

}
