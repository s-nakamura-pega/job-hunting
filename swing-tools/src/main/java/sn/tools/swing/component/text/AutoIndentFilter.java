package sn.tools.swing.component.text;

import javax.swing.text.*;

public class AutoIndentFilter extends DocumentFilter {

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
			throws BadLocationException {
		if ("\n".equals(string)) {
			string = autoIndent(fb.getDocument(), offset);
		}
		super.insertString(fb, offset, string, attr);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
		if ("\n".equals(text)) {
			text = autoIndent(fb.getDocument(), offset);
		}
		super.replace(fb, offset, length, text, attrs);
	}

	private String autoIndent(Document doc, int offset) throws BadLocationException {
		if (!(doc instanceof AbstractDocument)) {
			return "\n";
		}

		Element root = doc.getDefaultRootElement();
		int line = root.getElementIndex(offset);
		int lineStart = root.getElement(line).getStartOffset();

		String lineText = doc.getText(lineStart, offset - lineStart);

		StringBuilder indent = new StringBuilder();
		for (char c : lineText.toCharArray()) {
			if (c == ' ' || c == '\t')
				indent.append(c);
			else
				break;
		}

		return "\n" + indent.toString();
	}
}
