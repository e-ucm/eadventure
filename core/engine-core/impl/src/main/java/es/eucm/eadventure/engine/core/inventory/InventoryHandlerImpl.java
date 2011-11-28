package es.eucm.eadventure.engine.core.inventory;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;

@Singleton
public class InventoryHandlerImpl implements InventoryHandler {
	
	private long update = 0;
	
	private List<InventoryItem> inventory;
	
	public InventoryHandlerImpl( ){
		inventory = new ArrayList<InventoryItem>();
	}

	@Override
	public List<InventoryItem> getItems() {
		return inventory;
	}

	@Override
	public void add(EAdSceneElementDef item) {
		boolean add = true;
		for ( InventoryItem i: inventory ){
			if ( i.getElement() == item ){
				add = false;
				i.add(1);
				break;
			}
		}
		
		if ( add ){
			inventory.add(new InventoryItemImpl( item, 1 ));
		}
		update++;
	}

	@Override
	public void remove(EAdSceneElementDef item) {
		InventoryItem itemToRemove = null;
		for ( InventoryItem i: inventory ){
			if ( i.getElement() == item ){
				if ( i.getCount() == 1 ){
					itemToRemove = i;
					break;
				}
				else {
					i.add(-1);
				}
				break;
			}
		}
		
		if ( itemToRemove != null ){
			inventory.remove(itemToRemove);
		}
		update++;
	}

	@Override
	public long updateNumber() {
		return update;
	}

}
