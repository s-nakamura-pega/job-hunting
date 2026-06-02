package sn.tools.swing.window;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JTextArea textArea = new JTextArea();

	private final PrintStream originalOut;
	private final PrintStream originalErr;

	public ConsoleFrame() {
		setTitle("内部コンソールログ");
		setSize(600, 400);
		textArea.setEditable(false);
		add(new JScrollPane(textArea));
		this.originalOut = System.out;
		this.originalErr = System.err;
		TextAreaOutputStream taos = new TextAreaOutputStream();
		PrintStream ps = new PrintStream(taos, true);
		System.setOut(ps);
		System.setErr(ps);
	}

	@Override
	public void dispose() {
		// ウィンドウ破棄時に確実に元の状態に戻す
		System.setOut(originalOut);
		System.setErr(originalErr);
		super.dispose();
	}

	// 非staticインナークラス
	private class TextAreaOutputStream extends OutputStream {

		private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		@Override
		public void write(int b) {
			buffer.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) {
			buffer.write(b, off, len);
		}

		@Override
		public void flush() throws IOException {
			if (buffer.size() == 0) {
				return;
			}
			String text = buffer.toString();
			buffer.reset();
			SwingUtilities.invokeLater(() -> {
				textArea.append(text);
				textArea.setCaretPosition(textArea.getDocument().getLength());
			});
		}
	}

}
