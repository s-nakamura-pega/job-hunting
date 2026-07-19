package sn.tools.demo.shooting.object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import sn.tools.swing.game.object.GameObject2D;
import sn.tools.swing.util.KeyUtils.KeyAction;
import sn.tools.swing.util.definition.FocusTargetCondition;
import sn.tools.swing.util.definition.KeyModifiers;

public class Player extends GameObject2D {

	private final AtomicBoolean left = new AtomicBoolean(false);
	private final AtomicBoolean right = new AtomicBoolean(false);

	@Override
	public void init() {
		Dimension monitorSize = gameCanvasFunction.getCanvasSize().get();
		setX(monitorSize.width / 2 - 15);
		setY(monitorSize.height - 45);
	}

	@Override
	public void update() {
		if (left.get()) {
			setX(getX() - 5);
		}
		if (right.get()) {
			setX(getX() + 5);
		}
	}

	@Override
	public List<KeyAction> getKeyActionList() {
		return List.of(
				new KeyAction("leftOn", _ -> left.set(true), FocusTargetCondition.WINDOW_AND_COMPONENT,
						KeyEvent.VK_LEFT, false, KeyModifiers.NONE),
				new KeyAction("leftOff", _ -> left.set(false), FocusTargetCondition.WINDOW_AND_COMPONENT,
						KeyEvent.VK_LEFT, true, KeyModifiers.NONE),
				new KeyAction("rightOn", _ -> right.set(true), FocusTargetCondition.WINDOW_AND_COMPONENT,
						KeyEvent.VK_RIGHT, false, KeyModifiers.NONE),
				new KeyAction("rightOff", _ -> right.set(false), FocusTargetCondition.WINDOW_AND_COMPONENT,
						KeyEvent.VK_RIGHT, true, KeyModifiers.NONE),
				new KeyAction("spaceOn", _ -> gameCanvasFunction.addObject().accept(new Bullet(getX() + 10, getY())),
						FocusTargetCondition.WINDOW_AND_COMPONENT, KeyEvent.VK_SPACE, false, KeyModifiers.NONE));
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(getX(), getY(), 30, 30);
	}

	@Override
	protected Rectangle getRect() {
		return new Rectangle(getX(), getY(), 30, 30);
	}

}
