package sn.tools.swing.xml.component;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class XmlTextArea extends JTextArea implements XmlTextComponent {

    private static final long serialVersionUID = 1L;

	@Override
    public JTextComponent injectTargetTextComponent() {
        return this;
    }

}
