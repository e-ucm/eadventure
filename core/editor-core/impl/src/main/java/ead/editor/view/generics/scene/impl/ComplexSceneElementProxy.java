package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.variables.EAdVarDef;

public class ComplexSceneElementProxy extends ComplexSceneElement {

	public ComplexSceneElementProxy(EAdSceneElement element) {
		for (EAdVarDef<?> var : element.getVars().keySet()) {
			this.getVars().put(var, element.getVars().get(var));
		}
		this.setDefinition(element.getDefinition());
	}

}
