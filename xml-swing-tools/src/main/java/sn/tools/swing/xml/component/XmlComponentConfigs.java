package sn.tools.swing.xml.component;

import java.util.Map;

public interface XmlComponentConfigs {

	public static final Map<String, Class<? extends XmlComponent>> COMPONENT_CONFIGS = Map.ofEntries(
			Map.entry("label", XmlLabel.class),
			Map.entry("text", XmlTextField.class),
			Map.entry("text-area", XmlTextArea.class),
			Map.entry("button", XmlActionButton.class),
			Map.entry("check-box", XmlCheckBox.class),
			Map.entry("radio-button", XmlRadioButton.class)
		);

}
