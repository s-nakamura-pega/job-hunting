package sn.tools.demo.frame;

import sn.tools.swing.flow.frame.FlowScreenFrame;

public class MainFrame extends FlowScreenFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	protected String scanMenuBarPackage() {
		return "sn.tools.demo.menu";
	}

	@Override
	protected String initMenuBarId() {
		return "init";
	}

	@Override
	protected String scanScreenPackage() {
		return "sn.tools.demo.screen";
	}

	@Override
	protected String initScreenId() {
		return "customers_search";
	}

	@Override
	protected void onInit() {
		System.out.println("fram.onInit");
	}

}
