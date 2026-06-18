package sn.tools.swing.xml.panel;

import java.util.Map;

public interface XmlPanelConfigs {

	public static final Map<String, Class<? extends XmlPanel>> PANEL_CONFIGS = Map.ofEntries(
			Map.entry("flow-panel", XmlFlowPanel.class),
			Map.entry("grid-panel", XmlGridPanel.class),
			Map.entry("border-panel", XmlBorderPanel.class)
		);

}
