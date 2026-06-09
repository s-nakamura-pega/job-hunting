package sn.tools.swing.xml.frame;

import javax.swing.JFrame;

import sn.tools.swing.xml.controller.ScreenController;

public abstract class FlowScreenFrame {

	private final ScreenController controller;

	public FlowScreenFrame(String scanPackage, String initScreenId) {
		controller = new ScreenController(scanPackage);
		rootFrame().setContentPane(controller.getScreen(initScreenId));
		onInit();
	}

	protected abstract JFrame onInit();
	protected abstract JFrame rootFrame();
}
