package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.elementfactories.demos.scenes.EmptyScene;
import es.eucm.eadventure.common.model.elements.scene.EAdScene;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.resources.EAdResources;

public class EditionScene extends EmptyScene {

	public EditionScene(EAdScene scene) {
		
		EAdSceneElementDef elementDef = new SceneElementDefImpl();
		EAdResources oldResources = scene.getBackground().getDefinition().getResources();
		elementDef.getResources().addAsset(oldResources.getInitialBundle(), SceneElementDefImpl.appearance, oldResources.getAsset(oldResources.getInitialBundle(), SceneElementDefImpl.appearance));
		
		SceneElementImpl element = new SceneElementImpl();
		element.setDefinition(elementDef);
		element.setVarInitialValue(SceneElementImpl.VAR_SCALE, 1.0f);
		this.getComponents().add(element);
		
		for (EAdSceneElement sceneElement : scene.getComponents())
			this.getComponents().add(new EditionSceneElement(sceneElement, 1.0f));
	}
	
}
