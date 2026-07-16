package sn.tools.demo.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;

import sn.tools.swing.game.object.GameObject2D;
import sn.tools.swing.util.KeyUtils.KeyAction;

public class Player extends GameObject2D {

	private boolean left = false;
	private boolean right = false;
	private boolean space = false;
	private final Consumer<Bullet> addBullet;

	public Player(Consumer<Bullet> addBullet) {
		setX(200);
		setY(400);
		this.addBullet = addBullet;
	}

	@Override
	public void update() {
		if (left) {
			setX(getX() - 5);
		}
		if (right) {
			setX(getX() + 5);
		}
		if (space) {
			addBullet.accept(new Bullet(getX() + 10, getY()));
		}
	}

	@Override
	public List<KeyAction> getKeyActionList(JComponent component) {
		// TODO
		return List.of();
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
