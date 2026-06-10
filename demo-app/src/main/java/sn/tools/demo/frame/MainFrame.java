package sn.tools.demo.frame;

import javax.swing.SwingUtilities;

import sn.tools.swing.flow.frame.FlowScreenFrame;

public class MainFrame extends FlowScreenFrame {

	private static final long serialVersionUID = 1L;

	@Override
	protected String scanPackage() {
		return "sn.tools.demo.screen";
	}

	@Override
	protected String initScreenId() {
		return "init";
	}

	@Override
	protected void onInit() {
		System.out.println("fram.onInit");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame().setVisible(true);
		});
	}

}
