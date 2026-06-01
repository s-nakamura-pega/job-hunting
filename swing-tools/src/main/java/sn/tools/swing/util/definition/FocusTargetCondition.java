package sn.tools.swing.util.definition;

import java.util.List;

import javax.swing.JComponent;

public enum FocusTargetCondition {

	WINDOW(List.of(JComponent.WHEN_IN_FOCUSED_WINDOW)),
	COMPONENT(List.of(JComponent.WHEN_FOCUSED)),
	ANCESTOR(List.of(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)),
	WINDOW_AND_COMPONENT(List.of(JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED)),
	WINDOW_AND_ANCESTOR(List.of(JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)),
	COMPONENT_AND_ANCESTOR(List.of(JComponent.WHEN_FOCUSED, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)),
	WINDOW_AND_COMPONENT_AND_ANCESTOR(List.of(JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));

	private final List<Integer> targetList;

	public List<Integer> getTargetList() {
		return targetList;
	}

	private FocusTargetCondition(List<Integer> targetList) {
		this.targetList = targetList;
	}

}
