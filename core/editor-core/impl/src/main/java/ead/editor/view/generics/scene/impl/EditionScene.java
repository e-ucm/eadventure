package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.resources.EAdResources;
import ead.elementfactories.demos.scenes.EmptyScene;

public class EditionScene extends EmptyScene {

	public EditionScene(EAdScene scene) {
		
		EAdSceneElementDef elementDef = new SceneElementDefImpl();
		EAdResources oldResources = scene.getBackground().getDefinition().getResources();
		elementDef.getResources().addAsset(oldResources.getInitialBundle(), SceneElementDefImpl.appearance, oldResources.getAsset(oldResources.getInitialBundle(), SceneElementDefImpl.appearance));
		
		SceneElementImpl element = new SceneElementImpl();
		element.setDefinition(elementDef);
		element.setInitialScale(1.0f);
		this.getComponents().add(element);
		
		for (EAdSceneElement sceneElement : scene.getComponents())
			this.getComponents().add(new EditionSceneElement(sceneElement, 1.0f));
	}
	
}
