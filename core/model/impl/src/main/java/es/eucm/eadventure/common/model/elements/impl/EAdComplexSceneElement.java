package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;

public class EAdComplexSceneElement extends EAdBasicSceneElement
		implements EAdElement, EAdSceneElement {

	protected EAdElementList<EAdSceneElement> components;
	
	public EAdComplexSceneElement(String id) {
		super(id);
		components = new EAdElementListImpl<EAdSceneElement>(EAdSceneElement.class);
	}
	
	public EAdElementList<EAdSceneElement> getComponents() {
		return components;
	}
	
	

}
