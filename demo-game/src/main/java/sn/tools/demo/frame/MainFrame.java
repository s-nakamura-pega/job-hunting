package sn.tools.demo.frame;

import sn.tools.swing.flow.frame.FlowScreenFrame;

public class MainFrame extends FlowScreenFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		return null;
	}

	@Override
	protected void onInit() {
		System.out.println("frame.onInit");
	}

}