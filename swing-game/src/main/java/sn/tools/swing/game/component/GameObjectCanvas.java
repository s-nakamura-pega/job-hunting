package sn.tools.swing.game.component;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import sn.tools.swing.game.object.GameObject;

public class GameObjectCanvas extends AbstractCanvas {

    private static final long serialVersionUID = 1L;

    private final List<GameObject> objects = new ArrayList<>();

    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    @Override
    protected void update() {
        for (GameObject obj : objects) {
            obj.update();
        }
    }

    @Override
    protected void draw(Graphics g) {
        for (GameObject obj : objects) {
            obj.draw(g);
        }
    }
}
