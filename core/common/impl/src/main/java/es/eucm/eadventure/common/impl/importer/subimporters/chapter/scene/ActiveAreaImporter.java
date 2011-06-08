package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import java.awt.Point;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
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
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.IrregularShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;

public class ActiveAreaImporter implements
		Importer<ActiveArea, EAdSceneElement> {

	private Importer<Conditions, EAdCondition> conditionsImporter;
	private Importer<Action, EAdAction> actionImporter;
	private EAdElementFactory factory;

	@Inject
	public ActiveAreaImporter(
			Importer<Conditions, EAdCondition> conditionsImporter,
			Importer<Action, EAdAction> actionImporter,
			EAdElementFactory factory) {
		this.conditionsImporter = conditionsImporter;
		this.actionImporter = actionImporter;
		this.factory = factory;
	}

	@Override
	public EAdSceneElement convert(ActiveArea oldObject) {
		EAdBasicActor newActiveArea = (EAdBasicActor) factory
				.getActorByOldId(oldObject.getId());

		// Reference to the active area
		EAdActorReferenceImpl newActiveAreaReference = new EAdActorReferenceImpl(
				newActiveArea);
		
		importActions( oldObject, newActiveArea, newActiveAreaReference );

		Shape shape = null;
		if (oldObject.isRectangular()) {
			shape = new RectangleShape();
			((RectangleShape) shape).setHeight(oldObject.getHeight());
			((RectangleShape) shape).setWidth(oldObject.getWidth());
			newActiveAreaReference.setPosition(new EAdPosition(
					EAdPosition.Corner.TOP_LEFT, oldObject.getX(), oldObject
							.getY()));
			// FIXME deleted when active areas were working
			((RectangleShape) shape).setColor(EAdBorderedColor.BLACK_ON_WHITE);
		} else {
			shape = new IrregularShape();
			for (Point p : oldObject.getPoints()) {
				((IrregularShape) shape).getPositions().add(
						new EAdPosition(p.x, p.y));
			}
			// FIXME deleted when active areas were working
			((IrregularShape) shape).setColor(EAdBorderedColor.BLACK_ON_WHITE);
			newActiveAreaReference.setPosition(new EAdPosition(
					EAdPosition.Corner.TOP_LEFT, 0, 0));
		}
		newActiveArea.getResources().addAsset(newActiveArea.getInitialBundle(),
				EAdBasicSceneElement.appearance, shape);

		// Event to show (or not) the active area
		EAdCondition condition = conditionsImporter.convert(oldObject
				.getConditions());

		EAdConditionEventImpl event = new EAdConditionEventImpl(
				newActiveAreaReference.getId() + "_VisibleEvent");
		event.setCondition(condition);

		EAdChangeVarValueEffect visibleVar = new EAdChangeVarValueEffect(
				newActiveAreaReference.getId() + "_visibleEffect");
		visibleVar.setVar(newActiveAreaReference.visibleVar());
		BooleanOperation op = new BooleanOperation("booleanOpTrue");
		op.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);
		visibleVar.setOperation(op);
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
				visibleVar);

		EAdChangeVarValueEffect notVisibleVar = new EAdChangeVarValueEffect(
				newActiveArea.getId() + "_notVisibleEffect");
		notVisibleVar.setVar(newActiveAreaReference.visibleVar());
		op = new BooleanOperation("booleanOpFalse");
		op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
		notVisibleVar.setOperation(op);
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET,
				notVisibleVar);

		newActiveAreaReference.getEvents().add(event);
		return newActiveAreaReference;
	}
	
	private void importActions(ActiveArea oldObject, EAdBasicActor newActiveArea, EAdSceneElement newActiveAreaReference ){
		// Actions import
		for (Action a : oldObject.getActions()) {
			EAdAction action = actionImporter.convert(a);
			if (action != null)
				newActiveArea.getActions().add(action);
		}
		

		// Adding behavior

		if (newActiveArea.getActions().size() != 0) {
			EAdActorActionsEffect showActions = new EAdActorActionsEffect(
					newActiveArea.getId() + "_showActions",
					newActiveAreaReference);
			newActiveArea.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
					showActions);
		}
	}

	@Override
	public boolean equals(ActiveArea oldObject, EAdSceneElement newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
