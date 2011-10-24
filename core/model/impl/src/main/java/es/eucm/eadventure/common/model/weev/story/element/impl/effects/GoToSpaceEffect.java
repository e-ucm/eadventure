package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;
import es.eucm.eadventure.common.model.weev.world.Space;

/**
 * Effect to go to a new space (i.e. change the scene)
 */
@Element(detailed = GoToSpaceEffect.class, runtime = GoToSpaceEffect.class)
public class GoToSpaceEffect extends AbstractEffect {

	/**
	 * The new {@link Space}
	 */
	@Param(value = "space")
	private Space space;

	/**
	 * @param space
	 *            The new {@link Space}
	 */
	public GoToSpaceEffect(Space space) {
		this.space = space;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

}
