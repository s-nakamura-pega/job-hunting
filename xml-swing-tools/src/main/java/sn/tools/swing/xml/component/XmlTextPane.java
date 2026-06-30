package sn.tools.swing.xml.component;

import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.XmlObject;

@XmlObject("text-pane")
public class XmlTextPane extends XmlTextComponent {

	private final JTextPane component = new JTextPane();

	@Override
	public JTextComponent injectTargetTextComponent() {
		return component;
	}

	@InjectXmlAttribute("h-align")
	public void injectHorizontalAlignment(String alignment) {
		// JTextPane は setParagraphAttributes で整形する必要がある
		SimpleAttributeSet attrs = new SimpleAttributeSet();

		int align = switch (alignment.toLowerCase()) {
		case "center" -> StyleConstants.ALIGN_CENTER;
		case "right" -> StyleConstants.ALIGN_RIGHT;
		default -> StyleConstants.ALIGN_LEFT;
		};

		StyleConstants.setAlignment(attrs, align);
		component.setParagraphAttributes(attrs, false);
	}

}
