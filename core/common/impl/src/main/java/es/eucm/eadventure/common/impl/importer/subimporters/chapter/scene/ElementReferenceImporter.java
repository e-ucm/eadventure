package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;

/**
 * Elements reference importer
 * 
 */
public class ElementReferenceImporter implements
		Importer<ElementReference, EAdActorReference> {

	private EAdElementFactory factory;

	@Inject
	public ElementReferenceImporter(EAdElementFactory factory) {
		this.factory = factory;
	}

	/**
	 * Used to ensure unique identifiers in references
	 */
	private static int ID_GENERATOR = 0;

	@Override
	public EAdActorReference convert(ElementReference oldObject) {

		EAdActorReferenceImpl newRef = new EAdActorReferenceImpl(
				oldObject.getTargetId() + "_reference_" + ID_GENERATOR++);

		newRef.setPosition(new EAdPosition(EAdPosition.Corner.BOTTOM_CENTER,
				oldObject.getX(), oldObject.getY()));
		newRef.setScale(oldObject.getScale());
		newRef.setInitialOrientation(Orientation.SOUTH);
		EAdBasicActor actor = (EAdBasicActor) factory.getActorByOldId(oldObject
				.getTargetId());
		newRef.setReferencedActor(actor);

		if (actor.getActions().size() != 0) {
			EAdActorActionsEffect showActions = new EAdActorActionsEffect(actor.getId()
					+ "_showActions", newRef);
			newRef.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
		}
		return newRef;
	}

	@Override
	public boolean equals(ElementReference oldObject,
			EAdActorReference newObject) {

		if (oldObject.getX() != newObject.getPosition().getX()
				|| oldObject.getY() != newObject.getPosition().getY())
			return false;

		if (oldObject.getScale() != newObject.getScale())
			return false;

		if (factory.getActorByOldId(oldObject.getTargetId()) != newObject
				.getReferencedActor())
			return false;

		return true;
	}

}
