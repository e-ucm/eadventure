package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.assets.text.BasicFont;
import ead.common.model.elements.widgets.CheckBox;
import ead.common.model.elements.widgets.Label;
import ead.common.model.elements.widgets.TextArea;
import ead.common.model.elements.widgets.containers.ColumnContainer;

public class WidgetsScene extends EmptyScene {

	public WidgetsScene() {
		this.setId("WidgetsScene");
		ColumnContainer container = new ColumnContainer();
		for (int i = 0; i < 5; i++) {
			container.add(new Label("Label " + i));
		}
		container.add(new TextArea(200, 200));
		container.add(new CheckBox(false, "techdemo.widgets.checkbox",
				BasicFont.BIG));
		this.getSceneElements().add(container);
	}
}
