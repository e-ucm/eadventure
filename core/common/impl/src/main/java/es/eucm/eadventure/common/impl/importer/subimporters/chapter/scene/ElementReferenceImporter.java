package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
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
		EAdElementImporter<ElementReference, EAdActorReference> {

	private EAdElementFactory factory;

	@Inject
	public ElementReferenceImporter(EAdElementFactory factory) {
		this.factory = factory;
	}

	/**
	 * Used to ensure unique identifiers in references
	 */
	private static int ID_GENERATOR = 0;

	public EAdActorReference init(ElementReference oldObject) {
		EAdActorReferenceImpl newRef = new EAdActorReferenceImpl(
				oldObject.getTargetId() + "_reference_" + ID_GENERATOR++);
		return newRef;
	}
	
	@Override
	public EAdActorReference convert(ElementReference oldObject, Object object) {

		EAdActorReferenceImpl newRef = (EAdActorReferenceImpl) object;

		newRef.setPosition(new EAdPosition(EAdPosition.Corner.BOTTOM_CENTER,
				oldObject.getX(), oldObject.getY()));
		newRef.setScale(oldObject.getScale());
		newRef.setInitialOrientation(Orientation.SOUTH);
		EAdBasicActor actor = (EAdBasicActor) factory.getElementById(oldObject
				.getTargetId());
		newRef.setReferencedActor(actor);

		if (actor.getActions().size() != 0) {
			EAdActorActionsEffect showActions = new EAdActorActionsEffect(actor.getId()
					+ "_showActions", newRef);
			newRef.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
		}
		return newRef;
	}
}
