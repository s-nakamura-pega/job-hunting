package sn.tools.swing.flow.screen;

import javax.swing.JPanel;

import sn.tools.swing.flow.context.ScreenContext;

public interface ScreenCreator {

	void create();

	void reload();

	void onEnter(ScreenContext context);

	void onExit();

	JPanel getCreatedPanel();

	<T extends JPanel> T getCreatedPanel(Class<T> clazz);

}
