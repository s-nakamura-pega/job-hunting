package sn.tools.demo.shooting.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import sn.tools.swing.game.component.AbstractCanvas;

public class TitleCanvas extends AbstractCanvas {

    private static final long serialVersionUID = 1L;

    @Override
    protected void update() {
    }

    @Override
    protected void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("SHOOTING GAME", 80, 200);

        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("CLICK TO START", 120, 350);
    }

}

