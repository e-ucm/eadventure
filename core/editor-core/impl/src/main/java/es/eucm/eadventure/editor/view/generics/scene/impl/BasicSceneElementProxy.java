package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdVarDef;

public class BasicSceneElementProxy extends SceneElementImpl {

	public BasicSceneElementProxy(EAdSceneElement element) {
		setId(element.getId() + "_proxy");
		for (EAdVarDef<?> var : element.getVars().keySet()) {
			this.getVars().put(var, element.getVars().get(var));
		}
		this.setDefinition(new SceneElementDefProxy(element.getDefinition()));
	}

}
