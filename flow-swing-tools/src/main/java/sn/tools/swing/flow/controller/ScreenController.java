package sn.tools.swing.flow.controller;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sn.tools.clazz.creator.SimpleObjectCreator;
import sn.tools.swing.flow.annotation.Screen;
import sn.tools.swing.flow.creator.ScreenCreator;
import sn.tools.swing.flow.frame.FlowScreenFrame;
import sn.tools.swing.flow.screen.MenuScreen;

public class ScreenController extends Controller<JPanel, Screen> {

	private MenuScreen menuScreen;

	public ScreenController(String packageName, Class<? extends MenuScreen> menuScreen) {
		super(packageName, Screen.class);
		this.menuScreen = new SimpleObjectCreator<>(menuScreen).addConstructorArgument(List.class, getPanelCatalog()).create();
	}

	@Override
	protected Class<JPanel> getCreationClass() {
		return JPanel.class;
	}

	@Override
	protected String getCreatorAnnotationValue(Annotation annotation) {
		if (annotation instanceof Screen screen) {
			return screen.value();
		}
		throw new IllegalArgumentException(annotation.getClass().getName() + ": Screenアノテーションではありません。");
	}

	public List<ScreenCatalog> getPanelCatalog() {
		List<ScreenCatalog> catalogList = new ArrayList<ScreenCatalog>();
		creatorMap.forEach((k, v) -> {
			if (v instanceof ScreenCreator sc && sc.isDisplayCatalog()) {
				catalogList.add(new ScreenCatalog(k, sc.getScreenName(), sc.getScreenIcon()));
			}
		});
		return catalogList;
	}

	public void flowMenuScreen(FlowScreenFrame frame) {
		frame.flowScreen(menuScreen);
		String oldId = currentId.get();
		if (oldId != null) {
			SwingUtilities.invokeLater(() -> creatorMap.get(oldId).onExit());
		}
	}

	public static record ScreenCatalog(String id, String screenName, ImageIcon screenIcon) {
	}

}
