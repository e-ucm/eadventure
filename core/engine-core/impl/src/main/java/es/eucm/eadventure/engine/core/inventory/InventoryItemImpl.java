package es.eucm.eadventure.engine.core.inventory;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;

public class InventoryItemImpl implements InventoryItem {
	
	private EAdSceneElementDef item;
	
	private int count;
	
	public InventoryItemImpl(EAdSceneElementDef item, int count) {
		this.item = item;
		this.count = count;
	}

	@Override
	public EAdSceneElementDef getElement() {
		return item;
	}

	@Override
	public int getCount() {
		return count;
	}
	
	public void add( int count ){
		this.count += count;
	}

}
