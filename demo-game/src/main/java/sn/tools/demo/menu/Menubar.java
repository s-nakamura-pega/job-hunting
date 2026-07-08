package sn.tools.demo.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import sn.tools.swing.flow.annotation.MenuBar;
import sn.tools.swing.flow.creator.MenuBarCreator;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.parameter.ScreenParameter;

@MenuBar("init")
public class Menubar implements MenuBarCreator {

	private JMenuBar menuBar;

	@Override
	public void create() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem menuItem = new JMenuItem("Go to Menu");
		menuItem.addActionListener(FlowScreenFrame::flowMenu);
		menu.add(menuItem);
		menuBar.add(menu);
	}

	@Override
	public void onEnter(ScreenParameter parameter) {
	}

	@Override
	public void onDisplay(ScreenParameter parameter) {
	}

	@Override
	public void onExit() {
	}

	@Override
	public void reload() {
	}

	@Override
	public JMenuBar getCreation() {
		return menuBar;
	}

}
