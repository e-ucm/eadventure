package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.EAdListImpl;

public class EAdComplexSceneElement extends EAdBasicSceneElement
		implements EAdElement, EAdSceneElement {

	protected EAdList<EAdSceneElement> components;
	
	public EAdComplexSceneElement(String id) {
		super(id);
		components = new EAdListImpl<EAdSceneElement>(EAdSceneElement.class);
	}
	
	public EAdList<EAdSceneElement> getComponents() {
		return components;
	}
	
	

}
