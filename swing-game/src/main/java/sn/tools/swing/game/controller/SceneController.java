package sn.tools.swing.game.controller;

import java.lang.annotation.Annotation;
import sn.tools.swing.flow.controller.Controller;
import sn.tools.swing.flow.creator.Creator;
import sn.tools.swing.game.annotation.Scene;
import sn.tools.swing.game.component.AbstractCanvas;

public class SceneController extends Controller<AbstractCanvas, Scene> {

	public SceneController(String packageName) {
		super(packageName, Scene.class);
	}

	@Override
	protected Class<AbstractCanvas> getCreationClass() {
		return AbstractCanvas.class;
	}

	@Override
	protected String getCreatorAnnotationValue(Annotation annotation) {
		if (annotation instanceof Scene scene) {
			return scene.value();
		}
		throw new IllegalArgumentException(annotation.getClass().getName() + ": Sceneアノテーションではありません。");
	}

	public void start() {
		Creator<AbstractCanvas> creator = creatorMap.get(currentId.get());
		creator.getCreation().startLoop();
	}

	public void stop() {
		Creator<AbstractCanvas> creator = creatorMap.get(currentId.get());
		creator.getCreation().stopLoop();
	}

	public AbstractCanvas getCurrentCanvas() {
		if (currentId.get() == null) {
			return null;
		}
		return creatorMap.get(currentId.get()).getCreation();
	}

}
