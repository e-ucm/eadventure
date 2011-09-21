package es.eucm.eadventure.common.model.effects.impl.physics;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.params.geom.EAdPosition;

@Element(detailed = PhApplyForce.class, runtime = PhApplyForce.class)
public class PhApplyForce extends AbstractEAdEffect {

	@Param("force")
	private EAdPosition force;

	@Param("element")
	private EAdSceneElement element;

	public PhApplyForce(EAdSceneElement element, EAdPosition force) {
		this.element = element;
		this.force = force;
		this.setQueueable(false);
	}

	public EAdPosition getForce() {
		return force;
	}

	public EAdSceneElement getElement() {
		return element;
	}

}
