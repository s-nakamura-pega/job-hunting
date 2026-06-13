package sn.tools.demo.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import sn.tools.swing.flow.annotation.MenuBar;
import sn.tools.swing.flow.creator.MenuBarCreator;
import sn.tools.swing.flow.parameter.ScreenParameter;

@MenuBar("init")
public class DefaultMenuBar extends JMenuBar implements MenuBarCreator {

	private static final long serialVersionUID = 1L;

	@Override
	public void create() {
		add(new JMenu("test"));
	}

	@Override
	public void onEnter(ScreenParameter parameter) {
	}

	@Override
	public void onExit() {
	}

	@Override
	public void reload() {	
	}

	@Override
	public JMenuBar getCreation() {
		return this;
	}

}
