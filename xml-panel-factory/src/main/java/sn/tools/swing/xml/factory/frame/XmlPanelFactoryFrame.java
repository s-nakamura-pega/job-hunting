package sn.tools.swing.xml.factory.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;

import javax.swing.text.Style;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import sn.tools.swing.util.ComponentUtils;
import sn.tools.swing.util.WindowUtils;

public class XmlPanelFactoryFrame extends JFrame {

	public static final String DEFAULT_VALUE = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
			.append("<screen>\n")
			.append("\t<border-panel>\n")
			.append("\t\t<label font-style=\"BOLD\" font-size=\"20\">XmlPanelFactoryFrame</label>\n")
			.append("\t</border-panel>\n")
			.append("</screen>\n")
			.toString();
	private static final long serialVersionUID = 1L;
	private final XmlPrototypeFrame prototypeFrame = new XmlPrototypeFrame();

	private final JPanel mainPanel = new JPanel();
	private final BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.X_AXIS);

	private final JTextArea xmlWriterArea = new JTextArea();
	private final UndoManager undoManager;
	private final JScrollPane xmlWriterScroll = new JScrollPane(xmlWriterArea);

	private final JTextPane consoleArea = new JTextPane();
	private final JScrollPane consoleScroll = new JScrollPane(consoleArea);

	private final JPanel btnPanel = new JPanel();
	private final FlowLayout btnLayout = new FlowLayout();

	private final JButton executeBtn = new JButton("TEST");
	private final JButton saveBtn = new JButton("SAVE");
	private final JButton loadBtn = new JButton("LOAD");

	private final PrintStream originalOut;
	private final PrintStream originalErr;

	public XmlPanelFactoryFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension frameSize = WindowUtils.getScreenRatioSize(0.7);
		setSize(frameSize);
		setLocationRelativeTo(null);
		Dimension textSize = new Dimension(frameSize.width / 2, frameSize.height);
		xmlWriterScroll.setPreferredSize(textSize);
		xmlWriterArea.setText(DEFAULT_VALUE);
		xmlWriterArea.setFont(xmlWriterArea.getFont().deriveFont(16f));
		xmlWriterArea.setTabSize(2);
		undoManager = ComponentUtils.setUndo(xmlWriterArea);
		SwingUtilities.invokeLater(undoManager::discardAllEdits);
		consoleScroll.setPreferredSize(textSize);
		consoleArea.setFont(consoleArea.getFont().deriveFont(16f));
		consoleArea.setBackground(Color.DARK_GRAY);
		consoleArea.setEditable(false);
		mainPanel.setLayout(layout);
		mainPanel.add(xmlWriterScroll);
		mainPanel.add(consoleScroll);
		add(mainPanel, BorderLayout.CENTER);
		this.originalOut = System.out;
		this.originalErr = System.err;
		TextAreaOutputStream outStream = new TextAreaOutputStream(Color.WHITE);
		TextAreaOutputStream errStream = new TextAreaOutputStream(Color.RED);
		System.setOut(new PrintStream(outStream, true));
		System.setErr(new PrintStream(errStream, true));
		btnPanel.setLayout(btnLayout);
		btnLayout.setAlignment(FlowLayout.CENTER);
		btnPanel.add(executeBtn);
		btnPanel.add(loadBtn);
		btnPanel.add(saveBtn);
		add(btnPanel, BorderLayout.SOUTH);
		executeBtn.addActionListener(this::test);
		loadBtn.addActionListener(this::loadFile);
		saveBtn.addActionListener(this::saveFile);
	}

	public void test(ActionEvent event) {
		prototypeFrame.setPanel(xmlWriterArea.getText());
		if (!prototypeFrame.isVisible()) {
			prototypeFrame.setVisible(true);
		}
	}

	public void loadFile(ActionEvent event) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Load XML");

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				String text = Files.readString(file.toPath());
				xmlWriterArea.setText(text);
				System.out.println("Loaded: " + file.getAbsolutePath());
			} catch (IOException e) {
				System.err.println("Load failed: " + e.getMessage());
			}
		}
	}

	public void saveFile(ActionEvent event) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save XML");

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try (FileWriter fw = new FileWriter(file)) {
				fw.write(xmlWriterArea.getText());
				System.out.println("Saved: " + file.getAbsolutePath());
			} catch (IOException e) {
				System.err.println("Save failed: " + e.getMessage());
			}
		}
	}

	@Override
	public void dispose() {
		System.setOut(originalOut);
		System.setErr(originalErr);
		super.dispose();
	}

	private void appendConsole(String text, Color color) {
		StyledDocument doc = consoleArea.getStyledDocument();
		Style style = consoleArea.addStyle("style", null);
		StyleConstants.setForeground(style, color);
		try {
			doc.insertString(doc.getLength(), text, style);
			consoleArea.setCaretPosition(doc.getLength());
		} catch (Exception e) {
			originalErr.println("appendConsole failed:");
			e.printStackTrace(originalErr);
		}
	}

	private class TextAreaOutputStream extends OutputStream {

		private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		private final Color color;

		public TextAreaOutputStream(Color color) {
			this.color = color;
		}

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
			if (buffer.size() == 0)
				return;
			String text = buffer.toString();
			buffer.reset();
			SwingUtilities.invokeLater(() -> {
				appendConsole(text, color);
			});
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new XmlPanelFactoryFrame().setVisible(true));
	}

}
