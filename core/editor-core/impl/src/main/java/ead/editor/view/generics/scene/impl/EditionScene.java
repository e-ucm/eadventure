package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.resources.EAdResources;
import ead.elementfactories.demos.scenes.EmptyScene;

public class EditionScene extends EmptyScene {

	public EditionScene(EAdScene scene) {
		
		EAdSceneElementDef elementDef = new SceneElementDef();
		EAdResources oldResources = scene.getBackground().getDefinition().getResources();
		elementDef.getResources().addAsset(oldResources.getInitialBundle(), SceneElementDef.appearance, oldResources.getAsset(oldResources.getInitialBundle(), SceneElementDef.appearance));
		
		SceneElementImpl element = new SceneElementImpl();
		element.setDefinition(elementDef);
		element.setInitialScale(1.0f);
		this.getComponents().add(element);
		
		for (EAdSceneElement sceneElement : scene.getComponents())
			this.getComponents().add(new EditionSceneElement(sceneElement, 1.0f));
	}
	
}
