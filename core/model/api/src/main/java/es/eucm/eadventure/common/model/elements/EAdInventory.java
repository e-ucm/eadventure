package es.eucm.eadventure.common.model.elements;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.extra.EAdList;

public interface EAdInventory extends EAdElement {
	
	EAdList<EAdSceneElementDef> getInitialInventory();

}
