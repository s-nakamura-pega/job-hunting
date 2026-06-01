package sn.tools.swing.xml.component;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class XmlTextArea extends JTextArea implements XmlTextComponent {

    @Override
    public JTextComponent injectTargetTextComponent() {
        return this;
    }

}
