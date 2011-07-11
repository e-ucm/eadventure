package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdSystemEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSystemEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;

/**
 * Elements reference importer
 * 
 */
public class ElementReferenceImporter implements
		EAdElementImporter<ElementReference, EAdActorReference> {

	private EAdElementFactory factory;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;
	
	private EAdElementFactory elementFactory;
	
	@Inject
	public ElementReferenceImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory elementFactory){
		this.factory = factory;
		this.conditionsImporter = conditionsImporter;
		this.elementFactory = elementFactory;
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
		
		EAdCondition condition = conditionsImporter.init(oldObject.getConditions());
		condition = conditionsImporter
				.convert(oldObject.getConditions(), condition);

		EAdConditionEventImpl visibilityEvent = new EAdConditionEventImpl("visibilityCondition", condition);
		EAdChangeVarValueEffect visibilityEffect = new EAdChangeVarValueEffect("visibilityConditionEffect", newRef.visibleVar(), new BooleanOperation("", condition));
		visibilityEvent.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET, visibilityEffect);
		visibilityEvent.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET, visibilityEffect);
		newRef.getEvents().add(visibilityEvent);
		
		EAdSystemEventImpl startVisibilityEvent = new EAdSystemEventImpl("startVisibilityEvent");
		startVisibilityEvent.addEffect(EAdSystemEvent.Event.GAME_LOADED, visibilityEffect);
		elementFactory.getCurrentChapterModel().getEvents().add(startVisibilityEvent);

		if (actor.getActions().size() != 0) {
			EAdActorActionsEffect showActions = new EAdActorActionsEffect(actor.getId()
					+ "_showActions", newRef);
			newRef.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
		}
		return newRef;
	}
}
