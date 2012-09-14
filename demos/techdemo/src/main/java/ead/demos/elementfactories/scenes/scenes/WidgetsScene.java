package ead.demos.elementfactories.scenes.scenes;

import ead.common.widgets.Label;
import ead.common.widgets.containers.ColumnContainer;

public class WidgetsScene extends EmptyScene {
	
	public WidgetsScene( ){
		ColumnContainer container = new ColumnContainer( );
		for ( int i = 0; i < 10; i++ ){
			container.add(new Label("Label " + i));
		}
		this.getSceneElements().add(container);
	}

	
	@Override
	public String getSceneDescription() {
		return "A scene showing some widgets.";
	}

	public String getDemoName() {
		return "Widgets Scene";
	}
}
