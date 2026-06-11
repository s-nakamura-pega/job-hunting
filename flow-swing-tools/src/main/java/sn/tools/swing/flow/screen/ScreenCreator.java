package sn.tools.swing.flow.screen;

import javax.swing.JPanel;

import sn.tools.swing.flow.parameter.ScreenParameter;

public interface ScreenCreator {

	void create();

	void setScreenParameter(ScreenParameter parameter);

	JPanel getCreatedPanel();

	<T extends JPanel> T getCreatedPanel(Class<T> clazz);

}
