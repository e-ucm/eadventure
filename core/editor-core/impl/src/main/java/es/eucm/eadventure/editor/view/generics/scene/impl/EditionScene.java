package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.resources.EAdResources;

public class EditionScene extends EmptyScene {

	public EditionScene(EAdScene scene) {
		
		EAdSceneElementDef elementDef = new EAdSceneElementDefImpl();
		EAdResources oldResources = scene.getBackground().getDefinition().getResources();
		elementDef.getResources().addAsset(oldResources.getInitialBundle(), EAdSceneElementDefImpl.appearance, oldResources.getAsset(oldResources.getInitialBundle(), EAdSceneElementDefImpl.appearance));
		
		EAdBasicSceneElement element = new EAdBasicSceneElement();
		element.setDefinition(elementDef);
		element.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, 0.4f);
		this.getComponents().add(element);
		
	}
	
}
