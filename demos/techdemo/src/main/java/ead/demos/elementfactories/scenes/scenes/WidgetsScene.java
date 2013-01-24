package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.elements.widgets.Label;
import ead.common.model.elements.widgets.TextArea;
import ead.common.model.elements.widgets.containers.ColumnContainer;

public class WidgetsScene extends EmptyScene {

	public WidgetsScene() {
		this.setId("WidgetsScene");
		ColumnContainer container = new ColumnContainer();
		for (int i = 0; i < 10; i++) {
			container.add(new Label("Label " + i));
		}
		container.add(new TextArea(200, 200));
		this.getSceneElements().add(container);
	}
}
