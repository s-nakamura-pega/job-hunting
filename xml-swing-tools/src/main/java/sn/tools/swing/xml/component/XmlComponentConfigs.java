package sn.tools.swing.xml.component;

import java.util.Map;

public interface XmlComponentConfigs {

	public static final Map<String, Class<? extends XmlComponent>> COMPONENT_CONFIGS = Map.ofEntries(
			Map.entry("label", XmlLabel.class),
			Map.entry("text", XmlTextField.class),
			Map.entry("text-area", XmlTextArea.class),
			Map.entry("button", XmlActionButton.class),
			Map.entry("check-box", XmlCheckBox.class),
			Map.entry("radio-button", XmlRadioButton.class),
			Map.entry("tabs", XmlTabs.class),
			Map.entry("combo-box", XmlCombobox.class),
			Map.entry("list", XmlList.class),
			Map.entry("password", XmlPasswordField.class),
			Map.entry("scroll", XmlScroll.class),
			Map.entry("split", XmlSplitPane.class),
			Map.entry("table", XmlTable.class),
			Map.entry("text-pane", XmlTextPane.class)
		);

}
