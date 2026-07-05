package sn.tools.swing.flow.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class TitleScreen extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Timer timer;

	public TitleScreen() {
		setLayout(new BorderLayout());
		add(new Center(), BorderLayout.CENTER);
		South south = new South();
		add(south, BorderLayout.SOUTH);
		AtomicInteger index = new AtomicInteger(0);
		String[] dots = { ".", "..", "...", "...." };
		timer = new Timer(500, _ -> {
			int i = index.getAndUpdate(v -> (v + 1) % dots.length);
			south.setText(" Loading" + dots[i]);
		});
	}

	public void startProcessingTimer() {
		timer.start();
	}

	public void stopProcessingTimer() {
		timer.stop();
	}

	private class Center extends JLabel {

		private static final long serialVersionUID = 1L;

		private Center() {
			setText("Swing Framework");
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.CENTER);
			setFont(new Font("SansSerif", Font.PLAIN, 40));
			setForeground(Color.WHITE);
			setBackground(Color.DARK_GRAY);
			setOpaque(true);
		}

	}

	private class South extends JLabel {

		private static final long serialVersionUID = 1L;

		private South() {
			setText(" Loading.");
			setFont(new Font("SansSerif", Font.PLAIN, 20));
			setForeground(Color.WHITE);
			setBackground(Color.DARK_GRAY);
			setOpaque(true);
			setHorizontalAlignment(SwingConstants.LEFT);
		}

	}

}
