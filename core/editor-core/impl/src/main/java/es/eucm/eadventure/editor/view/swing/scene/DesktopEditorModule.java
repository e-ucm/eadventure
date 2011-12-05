package es.eucm.eadventure.editor.view.swing.scene;

import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;

public class DesktopEditorModule extends DesktopModule {
	
	protected void configureGUI() {
		bind(GUI.class).to(DesktopEditorGUI.class);
	}

}
