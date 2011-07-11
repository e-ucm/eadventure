package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;

@Element(detailed = EAdMoveActiveElement.class, runtime = EAdMoveActiveElement.class)
public class EAdMoveActiveElement extends AbstractEAdEffect {

	public EAdMoveActiveElement(String id) {
		super(id);
	}

}
