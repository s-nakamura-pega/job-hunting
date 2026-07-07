package sn.tools.swing.xml.factory.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Style;

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

import sn.tools.function.uncheck.Uncheck;
import sn.tools.swing.component.text.AutoCompleteAction;
import sn.tools.swing.component.text.AutoIndentFilter;
import sn.tools.swing.util.KeyUtils;
import sn.tools.swing.util.WindowUtils;
import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;
import sn.tools.swing.xml.annotation.InjectAction;
import sn.tools.swing.xml.annotation.InjectComponent;
import sn.tools.swing.xml.component.XmlComponent;
import sn.tools.swing.xml.component.XmlComponentConfigs;
import sn.tools.swing.xml.create.CreateUtils;
import sn.tools.swing.xml.injection.InjectionUtils;
import sn.tools.swing.xml.menu.XmlMenuBar;
import sn.tools.swing.xml.menu.XmlMenuItemComponent;
import sn.tools.swing.xml.panel.XmlPanel;
import sn.tools.swing.xml.panel.XmlPanelConfigs;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.util.XmlFormatUtils;

public class XmlPanelFactoryFrame extends JFrame {

	public static final String DEFAULT_VALUE = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
			.append("<screen>\n")
			.append("\t<border-panel>\n")
			.append("\t\t<label font-style=\"BOLD\" font-size=\"20\" h-align=\"center\">XmlPanelFactoryFrame</label>\n")
			.append("\t</border-panel>\n")
			.append("</screen>\n")
			.toString();
	private static final long serialVersionUID = 1L;
	private final XmlPrototypeFrame prototypeFrame = new XmlPrototypeFrame();

	private JTextArea xmlWriterArea;
	private JScrollPane xmlWriterScroll;

	private JTextPane consoleArea;
	private JScrollPane consoleScroll;

	private final PrintStream originalOut;
	private final PrintStream originalErr;

	private JMenu panelMenu;	
	private JMenu compMenu;

	private final Map<String, XmlComponent> mainCompMap = new ConcurrentHashMap<String, XmlComponent>();
	private final Map<String, XmlMenuItemComponent<?>> menuItemMap = new ConcurrentHashMap<String, XmlMenuItemComponent<?>>();
	private final Map<String, XmlComponent> dlgCompMap = new ConcurrentHashMap<String, XmlComponent>();
	private final List<String> tags = new ArrayList<>();

	private JPanel helpPanel;
	private JTextArea attr;
	private JTextArea elem;

	public XmlPanelFactoryFrame() {
		this.originalOut = System.out;
		this.originalErr = System.err;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension frameSize = WindowUtils.getScreenRatioSize(0.7);
		setSize(frameSize);
		setLocationRelativeTo(null);
		setTagList();
		setContentPane(frameSize);
		setHelpPanel();
		setMenubar();
	}

	private void setTagList() {
		XmlPanelConfigs.PANEL_CONFIGS.keySet().forEach(k -> tags.add("<" + k));
		XmlComponentConfigs.COMPONENT_CONFIGS.keySet().forEach(k -> tags.add("<" + k));
	}

	private void setContentPane(Dimension frameSize) {
		URL url = getClass().getClassLoader().getResource("sn/tools/swing/factory/xml/panel/main-panel.xml");
		XmlPanel xmlPanel = CreateUtils.createXmlPanelAndPutcomponentMap(url, mainCompMap);
		setContentPane(xmlPanel.injectTargetPanel());
		Uncheck.wrapRunnable(() -> InjectionUtils.injectComponent(this, mainCompMap)).run();
		Dimension textSize = new Dimension(frameSize.width / 2, frameSize.height);
		xmlWriterScroll.setPreferredSize(textSize);
		xmlWriterArea.setText(DEFAULT_VALUE);
		xmlWriterArea.setTabSize(2);
		AbstractDocument doc = (AbstractDocument) xmlWriterArea.getDocument();
		doc.setDocumentFilter(new AutoIndentFilter());
		KeyUtils.setKeyAndAction(
		        xmlWriterArea,
		        "xml-auto-complete",
		        new AutoCompleteAction(xmlWriterArea, tags),
		        FocusTargetCondition.COMPONENT,
		        KeyEvent.VK_SPACE,
		        KeyModifiers.CTRL
		);
		KeyUtils.setKeyAndAction(
		        xmlWriterArea,
		        "xml-format",
		        this::format,
		        FocusTargetCondition.COMPONENT,
		        KeyEvent.VK_F,
		        KeyModifiers.CTRL,
		        KeyModifiers.SHIFT
		);
		consoleScroll.setPreferredSize(textSize);
		consoleArea.setEditable(false);
		TextAreaOutputStream outStream = new TextAreaOutputStream(Color.WHITE);
		TextAreaOutputStream errStream = new TextAreaOutputStream(Color.RED);
		System.setOut(new PrintStream(outStream, true));
		System.setErr(new PrintStream(errStream, true));
	}

	private void setMenubar() {
		URL url = getClass().getClassLoader().getResource("sn/tools/swing/factory/xml/menu/menu.xml");
		XmlMenuBar xmlMenuBar = CreateUtils.createXmlMenuBar(url, menuItemMap);
		setJMenuBar(xmlMenuBar.injectTargetMenuBar());
		Uncheck.wrapRunnable(() -> InjectionUtils.injectMenuItem(this, menuItemMap)).run();
		setPanelMenu();
		setCompMenu();
	}

	private void setPanelMenu() {
		List<JMenuItem> list = new ArrayList<JMenuItem>();
		XmlPanelConfigs.PANEL_CONFIGS.forEach((k, v) -> {
			JMenuItem item = new JMenuItem(k);
			list.add(item);
			item.addActionListener(_ -> showHelpDialog(k, v));
		});
		list.sort(Comparator.comparing(JMenuItem::getText));
		list.forEach(panelMenu::add);
	}

	private void setCompMenu() {
		List<JMenuItem> list = new ArrayList<JMenuItem>();
		XmlComponentConfigs.COMPONENT_CONFIGS.forEach((k, v) -> {
			JMenuItem item = new JMenuItem(k);
			list.add(item);
			item.addActionListener(_ -> showHelpDialog(k, v));
		});
		list.sort(Comparator.comparing(JMenuItem::getText));
		list.forEach(compMenu::add);
	}

	private void showHelpDialog(String k, Class<?> v) {
		StringBuilder attrSb = new StringBuilder("-- Attributes --\n");
		Arrays.stream(v.getMethods()).filter(m -> m.isAnnotationPresent(InjectXmlAttribute.class))
				.map(m -> m.getAnnotation(InjectXmlAttribute.class)).forEach(att -> attrSb
						.append(String.format("%s %s\n", Arrays.toString(att.value()), att.explanation())));
		attr.setText(attrSb.toString());
		StringBuilder elemSb = new StringBuilder("-- Element Tag Regex --\n");
		Arrays.stream(v.getMethods()).filter(m -> m.isAnnotationPresent(InjectXmlElement.class))
				.map(m -> m.getAnnotation(InjectXmlElement.class)).forEach(elem -> elemSb
						.append(String.format("%s %s\n", Arrays.toString(elem.value()), elem.explanation())));
		elem.setText(elemSb.toString());
		JOptionPane.showMessageDialog(this, helpPanel, k, JOptionPane.PLAIN_MESSAGE);
	}

	private void setHelpPanel() {
		URL url = getClass().getClassLoader().getResource("sn/tools/swing/factory/xml/panel/help-panel.xml");
		CreateUtils.createXmlPanelAndPutcomponentMap(url, dlgCompMap);
		Uncheck.wrapRunnable(() -> InjectionUtils.injectComponent(this, dlgCompMap)).run();
	}

	@InjectAction("test")
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

	public void format(ActionEvent event) {
		String formatted = XmlFormatUtils.format(xmlWriterArea.getText());
		xmlWriterArea.setText(formatted);
	}

	public void autoComplete(ActionEvent event) {
		new AutoCompleteAction(xmlWriterArea, tags).actionPerformed(event);
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

	@InjectComponent("write-area")
	public void setXmlWriterArea(JTextArea xmlWriterArea) {
		this.xmlWriterArea = xmlWriterArea;
	}

	@InjectComponent("write-scroll")
	public void setXmlWriterScroll(JScrollPane xmlWriterScroll) {
		this.xmlWriterScroll = xmlWriterScroll;
	}

	@InjectComponent("console")
	public void setConsoleArea(JTextPane consoleArea) {
		this.consoleArea = consoleArea;
	}

	@InjectComponent("console-scroll")
	public void setConsoleScroll(JScrollPane consoleScroll) {
		this.consoleScroll = consoleScroll;
	}

	@InjectComponent("panel-menu")
	public void setPanelItem(JMenu panelMenu) {
		this.panelMenu = panelMenu;
	}

	@InjectComponent("comp-menu")
	public void setCompMenu(JMenu compMenu) {
		this.compMenu = compMenu;
	}

	@InjectComponent("help-panel")
	public void setHelpPanel(JPanel panel) {
		this.helpPanel = panel;
	}

	@InjectComponent("attr")
	public void setAttributeHelp(JTextArea textarea) {
		this.attr = textarea;
	}

	@InjectComponent("elem")
	public void setElementHelp(JTextArea textarea) {
		this.elem = textarea;
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

}
