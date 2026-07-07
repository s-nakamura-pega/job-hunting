package sn.tools.demo.system;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.demo.db.DBManager;
import sn.tools.demo.frame.MainFrame;

public class SystemMain {

	public static void main(String[] args) {
		initLogging();
		initDatabase();
		dumpInitialData();
		startUI();
	}

	private static void initLogging() {
		try {
			Path logPath = Paths.get("system.log");
			OutputStream os = Files.newOutputStream(logPath, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
			PrintStream logStream = new PrintStream(os, true, StandardCharsets.UTF_8);
			System.setOut(logStream);
			System.setErr(logStream);
		} catch (IOException e) {
			Exception root = ExceptionUtils.getRootCause(e);
			JOptionPane.showMessageDialog(null, root.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void initDatabase() {
		DBManager.initDB();
	}

	private static void dumpInitialData() {
		var exec = DBManager.getDBExecutor();
		System.out.println("-- customers");
		System.out.println(exec.query("select * from customers"));
		System.out.println();

		System.out.println("-- orders");
		System.out.println(exec.query("select * from orders"));
		System.out.println();

		System.out.println("-- order_items");
		System.out.println(exec.query("select * from order_items"));
		System.out.println();
	}

	private static void startUI() {
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}

}
