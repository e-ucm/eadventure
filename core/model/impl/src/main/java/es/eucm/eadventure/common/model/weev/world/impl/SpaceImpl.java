package es.eucm.eadventure.common.model.weev.world.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.weev.world.Space;

/**
 * Default {@link Space} implementation
 */
@Element(detailed = SpaceImpl.class, runtime = SpaceImpl.class)
public class SpaceImpl extends EAdSceneImpl implements Space {

	@Param(value = "x")
	private int x;
	
	@Param(value = "y")
	private int y;
	
	public SpaceImpl() {
		super();
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
