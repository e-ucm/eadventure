package es.eucm.eadventure.common.model.impl.inventory;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.EAdInventory;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;

@Element(detailed = EAdBasicInventory.class, runtime = EAdBasicInventory.class)
public class EAdBasicInventory extends AbstractEAdElement implements
		EAdInventory {

	public EAdBasicInventory() {
		super("inventory");
	}

}
