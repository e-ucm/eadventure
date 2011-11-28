package es.eucm.eadventure.engine.core.inventory;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;

public interface InventoryItem {
	
	EAdSceneElementDef getElement();
	
	int getCount();
	
	void add( int count );

}
