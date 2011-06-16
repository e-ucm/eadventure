package es.eucm.eadventure.common.model.elements.test;

import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;

public class ActorReferenceFactoryTest {

	public static EAdActorReference getActorReference(EAdActor actor) {
		EAdActorReferenceImpl actorReference = new EAdActorReferenceImpl(actor.getId() + "referece", actor);
		
		EAdActorActionsEffect showActions = new EAdActorActionsEffect( actorReference.getId()+ "_showActions", actorReference);
		actorReference.getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
		
		actorReference.setPosition(new EAdPosition(400, 400));
		actorReference.setScale(0.5f);
		return actorReference;
	}
}
