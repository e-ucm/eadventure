package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.params.geom.EAdPosition;

/**
 * Effect that adds an actor reference to the current scene
 * 
 * 
 */
@Element(detailed = EAdAddActorReferenceEffect.class, runtime = EAdAddActorReferenceEffect.class)
public class EAdAddActorReferenceEffect extends AbstractEAdEffect {

	@Param("actor")
	private EAdSceneElementDef actor;

	@Param("position")
	private EAdPosition position;

	@Param("initialEffect")
	private EAdSceneElementEffect effect;

	public EAdAddActorReferenceEffect() {

	}

	public EAdAddActorReferenceEffect(EAdSceneElementDef actor, EAdPosition p,
			EAdSceneElementEffect effect) {
		this.actor = actor;
		this.position = p;
		this.effect = effect;
	}

	public EAdSceneElementDef getActor() {
		return actor;
	}

	public EAdPosition getPosition() {
		return position;
	}

	public EAdSceneElementEffect getInitialEffect() {
		return effect;
	}

}
