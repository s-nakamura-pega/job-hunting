package sn.tools.demo.shooting.scene;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sn.tools.demo.shooting.canvas.GameClearCanvas;
import sn.tools.swing.flow.parameter.Parameter;
import sn.tools.swing.flow.parameter.SimpleParameter;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;
import sn.tools.swing.game.creator.SceneCreator;
import sn.tools.swing.game.panel.GamePanel;

@Scene("gameclear")
public class GameClearSceneCreator extends SceneCreator {

    private GameClearCanvas canvas;

    @Override
    public void create() {
        canvas = new GameClearCanvas();

        canvas.addMouseListenerEx(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GamePanel.flowScene(canvas, "title", new SimpleParameter());
            }
        });
    }

    @Override
    public AbstractCanvas getCreation() {
        return canvas;
    }

    @Override
    protected void init(Parameter parameter) {
    }

    @Override
    protected void cleanup() {
    }
}
