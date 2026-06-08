package sn.tools.swing.xml.panel;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import sn.tools.clazz.creator.ObjectCreator.ConstructorArgument;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public interface XmlPanelConfigs {

	public static final Map<String, PanelConfig<?>> PANEL_CONFIGS = Map
			.ofEntries(Map.entry("flow-panel", new PanelConfig<>(XmlFlowPanel.class, List.of())));

	public static record PanelConfig<R extends XmlPanel>(Class<R> clazz, List<ConstructorArgument<?>> argList) {

		public R getPanel(Element element) {
			return new XmlObjectCreator<>(element, clazz).addAllConstructorArgument(argList).create();
		}

	}

}
