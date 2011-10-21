package es.eucm.eadventure.common.model.weev.impl;

import es.eucm.eadventure.common.model.impl.EAdElementImpl;
import es.eucm.eadventure.common.model.weev.WEEVElement;
import es.eucm.eadventure.common.model.weev.common.Positioned;

/**
 * Abstract implementation of {@link WEEVElement}
 */
public abstract class AbstractPositionedWEEVElement extends EAdElementImpl implements
		WEEVElement, Positioned {

	private int x;

	private int y;

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

}
