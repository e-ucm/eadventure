package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdInventory;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;

@Element(detailed = EAdInventoryImpl.class, runtime = EAdInventoryImpl.class)
public class EAdInventoryImpl extends EAdElementImpl implements EAdInventory {

	public static final EAdVarDef<Boolean> VAR_IN_INVENTORY = new EAdVarDefImpl<Boolean>(
			"in_inventory", Boolean.class, false);

	@Param("initialItems")
	private EAdList<EAdSceneElementDef> initialItems;

	public EAdInventoryImpl() {
		this.setId("inventory");
		initialItems = new EAdListImpl<EAdSceneElementDef>(
				EAdSceneElementDef.class);
	}

	@Override
	public EAdList<EAdSceneElementDef> getInitialItems() {
		return initialItems;
	}

}
