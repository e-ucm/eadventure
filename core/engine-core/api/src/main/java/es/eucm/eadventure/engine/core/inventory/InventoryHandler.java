package es.eucm.eadventure.engine.core.inventory;

import java.util.List;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;

public interface InventoryHandler {
	
	List<InventoryItem> getItems();
	
	void add( EAdSceneElementDef item );
	
	void remove( EAdSceneElementDef item );
	
	long updateNumber();

}
