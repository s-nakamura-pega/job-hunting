package sn.tools.swing.util.definition;

import java.awt.event.InputEvent;

public enum KeyModifiers {

	NONE(0),
	CTRL(InputEvent.CTRL_DOWN_MASK),
	SHIFT(InputEvent.SHIFT_DOWN_MASK),
	ALT(InputEvent.ALT_DOWN_MASK),
	META(InputEvent.META_DOWN_MASK);

	private final int mask;

	KeyModifiers(int mask) {

		this.mask = mask;
	}

	public int mask() {
		return mask;
	}

	public static int of(KeyModifiers... mods) {
		int m = 0;
		for (KeyModifiers mod : mods) {
			m |= mod.mask;
		}
		return m;
	}

}
