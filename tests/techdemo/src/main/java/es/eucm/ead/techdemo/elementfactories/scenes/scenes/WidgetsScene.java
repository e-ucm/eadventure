package es.eucm.ead.techdemo.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.elements.widgets.CheckBox;
import es.eucm.ead.model.elements.widgets.Label;
import es.eucm.ead.model.elements.widgets.TextArea;
import es.eucm.ead.model.elements.widgets.containers.ColumnContainer;

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
