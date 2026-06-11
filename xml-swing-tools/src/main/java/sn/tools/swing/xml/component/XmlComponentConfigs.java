package sn.tools.swing.xml.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface XmlComponentConfigs {

	public static final Map<String, Class<? extends XmlComponent>> COMPONENT_CONFIGS = new ConcurrentHashMap<String, Class<? extends XmlComponent>>() {
		{
			put("label", XmlLabel.class);
			put("text", XmlTextField.class);
			put("text-area", XmlTextArea.class);
			put("button", XmlActionButton.class);
			put("check-box", XmlCheckBox.class);
			put("radio-button", XmlRadioButton.class);
		}
	};

}
