package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

public class ComplexSceneElementReplica extends EAdComplexElementImpl {

	public ComplexSceneElementReplica(EAdSceneElement element) {
		for (EAdVarDef<?> var : element.getVars().keySet()) {
			this.getVars().put(var, element.getVars().get(var));
		}
		this.setDefinition(element.getDefinition());
	}

}
