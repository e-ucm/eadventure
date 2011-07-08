package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ActorImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;

public class ActiveAreaImporter implements
		EAdElementImporter<ActiveArea, EAdSceneElement> {

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EAdElementImporter<Action, EAdAction> actionImporter;

	private StringHandler stringHandler;
	
	@Inject
	public ActiveAreaImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementImporter<Action, EAdAction> actionImporter,
			StringHandler stringHandler) {
		this.conditionsImporter = conditionsImporter;
		this.actionImporter = actionImporter;
		this.stringHandler = stringHandler;
	}

	@Override
	public EAdSceneElement init(ActiveArea oldObject) {
		EAdBasicActor newActiveArea = new EAdBasicActor(oldObject.getId());
		EAdActorReferenceImpl newActiveAreaReference = new EAdActorReferenceImpl(
				newActiveArea);
		return newActiveAreaReference;
	}
	
	@Override
	public EAdSceneElement convert(ActiveArea oldObject, Object object) {
		// Reference to the active area
		EAdActorReferenceImpl newActiveAreaReference = (EAdActorReferenceImpl) object;

		EAdBasicActor newActiveArea = (EAdBasicActor) newActiveAreaReference.getReferencedActor();

		ActorImporter.addActions(oldObject, newActiveArea, actionImporter, stringHandler);
		EAdActorActionsEffect showActions = new EAdActorActionsEffect(newActiveArea.getId()
				+ "_showActions", newActiveAreaReference);
		newActiveAreaReference.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);

		
		Shape shape = ShapedElementImporter.importShape(oldObject, newActiveAreaReference);

		newActiveArea.getResources().addAsset(newActiveArea.getInitialBundle(),
				EAdBasicSceneElement.appearance, shape);
		
		Shape shape2 = ShapedElementImporter.importShape(oldObject, newActiveAreaReference);
		shape2.setColor(EAdBorderedColor.BLACK_ON_WHITE);
		EAdBundleId id = new EAdBundleId("id");
		newActiveArea.getResources().addAsset(id, EAdBasicSceneElement.appearance, shape2);
		newActiveAreaReference.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, new EAdChangeAppearance("test", newActiveArea, id));
		newActiveAreaReference.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, new EAdChangeAppearance("test", newActiveArea, newActiveArea.getInitialBundle()));
		


		// Event to show (or not) the active area
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject
				.getConditions(), condition);

		EAdConditionEventImpl event = new EAdConditionEventImpl(
				newActiveAreaReference.getId() + "_VisibleEvent");
		event.setCondition(condition);

		EAdChangeVarValueEffect visibleVar = new EAdChangeVarValueEffect(
				newActiveAreaReference.getId() + "_visibleEffect");
		visibleVar.addVar(newActiveAreaReference.visibleVar());
		BooleanOperation op = new BooleanOperation("booleanOpTrue");
		op.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);
		visibleVar.setOperation(op);
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
				visibleVar);

		EAdChangeVarValueEffect notVisibleVar = new EAdChangeVarValueEffect(
				newActiveArea.getId() + "_notVisibleEffect");
		notVisibleVar.addVar(newActiveAreaReference.visibleVar());
		op = new BooleanOperation("booleanOpFalse");
		op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
		notVisibleVar.setOperation(op);
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET,
				notVisibleVar);

		newActiveAreaReference.getEvents().add(event);
		return newActiveAreaReference;
	}

}
