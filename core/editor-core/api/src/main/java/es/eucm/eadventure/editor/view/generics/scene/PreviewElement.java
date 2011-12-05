package es.eucm.eadventure.editor.view.generics.scene;

import java.util.List;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;

public interface PreviewElement {
	
	void setSceneElement(EAdSceneElement sceneElement);

	List<PanelListener> getPanelListener();
	
}
