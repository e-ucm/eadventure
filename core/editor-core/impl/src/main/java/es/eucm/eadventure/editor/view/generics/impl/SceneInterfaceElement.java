package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;

public class SceneInterfaceElement implements InterfaceElement {

	private EAdScene scene;
	
	public SceneInterfaceElement(EAdScene scene) {
		this.scene = scene;
	}

	public EAdScene getScene() {
		return scene;
	}
}
