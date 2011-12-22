package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scenes.ComplexSceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdVarDef;

public class ComplexSceneElementProxy extends ComplexSceneElementImpl {

	public ComplexSceneElementProxy(EAdSceneElement element) {
		for (EAdVarDef<?> var : element.getVars().keySet()) {
			this.getVars().put(var, element.getVars().get(var));
		}
		this.setDefinition(element.getDefinition());
	}

}
