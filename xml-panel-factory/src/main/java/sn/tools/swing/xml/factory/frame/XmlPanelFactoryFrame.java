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
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.Style;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.util.ComponentUtils;
import sn.tools.swing.util.WindowUtils;
import sn.tools.swing.xml.annotation.InjectComponent;
import sn.tools.swing.xml.component.XmlComponentConfigs;
import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.swing.xml.injection.InjectionUtils;
import sn.tools.swing.xml.menu.XmlMenuBar;
import sn.tools.swing.xml.menu.XmlMenuItemComponent;
import sn.tools.swing.xml.panel.XmlPanelConfigs;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;

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

	private final PrintStream originalOut;
	private final PrintStream originalErr;

	private JMenu panelMenu;	
	private JMenu compMenu;

	private final Map<String, XmlMenuItemComponent<?>> componentMap = new ConcurrentHashMap<String, XmlMenuItemComponent<?>>();

	public XmlPanelFactoryFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension frameSize = WindowUtils.getScreenRatioSize(0.7);
		setSize(frameSize);
		setLocationRelativeTo(null);
		setMenubar();
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
		add(btnPanel, BorderLayout.SOUTH);
		executeBtn.addActionListener(this::test);
	}

	private void setMenubar() {
		URL url = getClass().getClassLoader().getResource("sn/tools/swing/factory/frame/menu/menu.xml");
		XmlMenuBar xmlMenuBar = CreateUtils.createXmlMenuBar(url, componentMap);
		setJMenuBar(xmlMenuBar.injectTargetMenuBar());
		Uncheck.wrapRunnable(() -> InjectionUtils.injectMenuItem(this, componentMap)).run();
		setPanelMenu();
		setCompMenu();
	}

	private void setPanelMenu() {
		XmlPanelConfigs.PANEL_CONFIGS.forEach((k, v) -> {
			JMenuItem item = new JMenuItem(k);
			panelMenu.add(item);
			StringBuilder sb = new StringBuilder("-- Attributes --\n");
			Arrays.stream(v.getMethods()).filter(m -> m.isAnnotationPresent(InjectXmlAttribute.class))
					.map(m -> m.getAnnotation(InjectXmlAttribute.class))
					.forEach(att -> sb.append(String.format("%s %s\n", Arrays.toString(att.value()), att.explanation())));
			item.addActionListener(_ -> JOptionPane.showMessageDialog(this, sb.toString()));
		});
	}

	private void setCompMenu() {
		XmlComponentConfigs.COMPONENT_CONFIGS.forEach((k, v) -> {
			JMenuItem item = new JMenuItem(k);
			compMenu.add(item);
			StringBuilder sb = new StringBuilder("-- Attributes --\n");
			Arrays.stream(v.getMethods()).filter(m -> m.isAnnotationPresent(InjectXmlAttribute.class))
					.map(m -> m.getAnnotation(InjectXmlAttribute.class)).forEach(att -> sb
							.append(String.format("%s %s\n", Arrays.toString(att.value()), att.explanation())));
			item.addActionListener(_ -> JOptionPane.showMessageDialog(this, sb.toString()));
		});
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

	@InjectComponent("panel-menu")
	public void setPanelItem(JMenu panelMenu) {
		this.panelMenu = panelMenu;
	}

	@InjectComponent("comp-menu")
	public void setCompMenu(JMenu compMenu) {
		this.compMenu = compMenu;
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
