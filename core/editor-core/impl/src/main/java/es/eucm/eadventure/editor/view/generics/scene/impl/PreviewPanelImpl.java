package es.eucm.eadventure.editor.view.generics.scene.impl;

import java.util.List;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.editor.view.generics.scene.PreviewElement;
import es.eucm.eadventure.editor.view.generics.scene.PreviewPanel;

public class PreviewPanelImpl implements PreviewPanel {

	private EAdScene scene;
	
	public PreviewPanelImpl(EAdScene scene) {
		this.scene = scene;
	}

	@Override
	public EAdScene getScene() {
		return scene;
	}

	@Override
	public void setScene(EAdScene scene) {
		this.scene = scene;
	}

	@Override
	public List<PreviewElement> getElements() {
		// TODO Auto-generated method stub
		return null;
	}

}
