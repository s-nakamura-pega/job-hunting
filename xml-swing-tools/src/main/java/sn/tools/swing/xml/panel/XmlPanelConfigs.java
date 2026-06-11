package sn.tools.swing.xml.panel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface XmlPanelConfigs {

	public static final Map<String, Class<? extends XmlPanel>> PANEL_CONFIGS = new ConcurrentHashMap<String, Class<? extends XmlPanel>>() {
		{
			put("flow-panel", XmlFlowPanel.class);
		}
	};

}
