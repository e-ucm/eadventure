package es.eucm.eadventure.common.model.weev.world.impl;

import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.weev.world.Space;

/**
 * Default {@link Space} implementation
 */
public class SpaceImpl extends EAdSceneImpl implements Space {

	private int x;
	
	private int y;
	
	public SpaceImpl(String id) {
		super(id);
	}
	
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
