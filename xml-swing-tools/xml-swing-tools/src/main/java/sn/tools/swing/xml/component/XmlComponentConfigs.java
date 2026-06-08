package sn.tools.swing.xml.component;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import sn.tools.clazz.creator.ObjectCreator.ConstructorArgument;
import sn.tools.xml.bind.creator.XmlObjectCreator;

public interface XmlComponentConfigs {

	public static final Map<String, ComponentConfig<?>> COMPONENT_CONFIGS = Map.ofEntries(
			Map.entry("text", new ComponentConfig<>(XmlTextField.class, List.of())),
			Map.entry("button", new ComponentConfig<>(XmlActionButton.class, List.of())),
			Map.entry("check-box", new ComponentConfig<>(XmlCheckBox.class, List.of())),
			Map.entry("radio-button", new ComponentConfig<>(XmlRadioButton.class, List.of())),
			Map.entry("text-area", new ComponentConfig<>(XmlTextArea.class, List.of())),
			Map.entry("label", new ComponentConfig<>(XmlLabel.class, List.of())));

	public static record ComponentConfig<R extends XmlComponent>(Class<R> clazz, List<ConstructorArgument<?>> argList) {

		public R getComponent(Element element) {
			return new XmlObjectCreator<>(element, clazz).addAllConstructorArgument(argList).create();
		}

	}

}
