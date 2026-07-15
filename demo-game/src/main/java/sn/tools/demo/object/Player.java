package sn.tools.demo.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sn.tools.swing.game.object.GameObject2D;

public class Player extends GameObject2D {

    private int x = 200;
    private int y = 400;

    @Override
    public void update() {
        if (Key.left) x -= 5;
        if (Key.right) x += 5;

        if (Key.space) {
            getCanvas().addObject(new Bullet(x + 10, y));
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, 30, 30);
    }

    @Override
    protected Rectangle getRect() {
        return new Rectangle(x, y, 30, 30);
    }
}

