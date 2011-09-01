/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.StringsWriter;
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
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;

public class ActiveAreaImporter implements
		EAdElementImporter<ActiveArea, EAdSceneElement> {

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EAdElementImporter<Action, EAdAction> actionImporter;

	private StringsWriter stringHandler;

	@Inject
	public ActiveAreaImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementImporter<Action, EAdAction> actionImporter,
			StringsWriter stringHandler) {
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

		EAdBasicActor newActiveArea = (EAdBasicActor) newActiveAreaReference
				.getReferencedActor();

		ActorImporter.addActions(oldObject, newActiveArea, actionImporter,
				stringHandler);
		EAdActorActionsEffect showActions = new EAdActorActionsEffect(
				newActiveArea.getId() + "_showActions", newActiveAreaReference);
		newActiveAreaReference.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				showActions);

		newActiveArea.setName(stringHandler.addString(oldObject.getName()));
		newActiveArea.setDescription(stringHandler.addString(oldObject
				.getDescription()));

		newActiveArea.setDetailedDescription(stringHandler.addString(oldObject
				.getDetailedDescription()));

		Shape shape = ShapedElementImporter.importShape(oldObject,
				newActiveAreaReference);

		newActiveArea.getResources().addAsset(newActiveArea.getInitialBundle(),
				EAdBasicSceneElement.appearance, shape);

		Shape shape2 = ShapedElementImporter.importShape(oldObject,
				newActiveAreaReference);
		EAdBundleId id = new EAdBundleId("id");
		newActiveArea.getResources().addAsset(id,
				EAdBasicSceneElement.appearance, shape2);
		newActiveAreaReference.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED,
				new EAdChangeAppearance("test", newActiveArea, id));
		newActiveAreaReference.addBehavior(
				EAdMouseEventImpl.MOUSE_EXITED,
				new EAdChangeAppearance("test", newActiveArea, newActiveArea
						.getInitialBundle()));

		// Event to show (or not) the active area
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		EAdConditionEventImpl event = new EAdConditionEventImpl(
				newActiveAreaReference.getId() + "_VisibleEvent");
		event.setCondition(condition);

		EAdChangeVarValueEffect visibleVar = new EAdChangeVarValueEffect(
				newActiveAreaReference.getId() + "_visibleEffect");
		visibleVar.addVar(newActiveAreaReference.getVars().getVar(
				EAdSceneElementVars.VAR_VISIBLE));
		BooleanOperation op = new BooleanOperation("booleanOpTrue");
		op.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);
		visibleVar.setOperation(op);
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
				visibleVar);

		EAdChangeVarValueEffect notVisibleVar = new EAdChangeVarValueEffect(
				newActiveArea.getId() + "_notVisibleEffect");
		notVisibleVar.addVar(newActiveAreaReference.getVars().getVar(
				EAdSceneElementVars.VAR_VISIBLE));
		op = new BooleanOperation("booleanOpFalse");
		op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
		notVisibleVar.setOperation(op);
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET,
				notVisibleVar);

		newActiveAreaReference.getEvents().add(event);
		return newActiveAreaReference;
	}

}
