package es.eucm.eadventure.editor.view.generics.scene;

import java.util.List;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;

public interface PreviewPanel extends InterfaceElement {

	EAdScene getScene();
	
	void setScene(EAdScene scene);
	
	List<PreviewElement> getElements();
	
}
