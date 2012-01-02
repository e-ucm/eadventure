package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.EAdVarDef;

public class BasicSceneElementProxy extends SceneElementImpl {

	public BasicSceneElementProxy(EAdSceneElement element) {
		setId(element.getId() + "_proxy");
		for (EAdVarDef<?> var : element.getVars().keySet()) {
			this.getVars().put(var, element.getVars().get(var));
		}
		this.setDefinition(new SceneElementDefProxy(element.getDefinition()));
	}

}
