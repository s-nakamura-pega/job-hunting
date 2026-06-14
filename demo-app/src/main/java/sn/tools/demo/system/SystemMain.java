package sn.tools.demo.system;

import javax.swing.SwingUtilities;

import sn.tools.demo.frame.MainFrame;

public class SystemMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}

}
