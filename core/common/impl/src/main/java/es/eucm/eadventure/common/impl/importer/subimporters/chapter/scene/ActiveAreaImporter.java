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
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ActorImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeAppearance;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;

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
		EAdSceneElementDefImpl newActiveArea = new EAdSceneElementDefImpl();
		newActiveArea.setId(oldObject.getId());
		EAdSceneElement newActiveAreaReference = new EAdBasicSceneElement(
				newActiveArea);
		return newActiveAreaReference;
	}

	@Override
	public EAdSceneElement convert(ActiveArea oldObject, Object object) {
		// Reference to the active area
		EAdBasicSceneElement newActiveAreaReference = (EAdBasicSceneElement) object;

		EAdSceneElementDefImpl newActiveArea = (EAdSceneElementDefImpl) newActiveAreaReference
				.getDefinition();

		ActorImporter.addActions(oldObject, newActiveArea, actionImporter,
				stringHandler);
		EAdActorActionsEffect showActions = new EAdActorActionsEffect( newActiveAreaReference);
		showActions.setId(
				newActiveArea.getId() + "_showActions");
		newActiveAreaReference.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				showActions);

		stringHandler.setString(newActiveArea.getName(), oldObject.getName());
		stringHandler.setString(newActiveArea.getDescription(),
				oldObject.getDescription());
		stringHandler.setString(newActiveArea.getDetailedDescription(),
				oldObject.getDetailedDescription());
		stringHandler.setString(newActiveArea.getDocumentation(),
				oldObject.getDocumentation());

		if (oldObject.getInfluenceArea() != null) {
			newActiveAreaReference.setVarInitialValue(
					NodeTrajectoryDefinition.VAR_INFLUENCE_AREA,
					new EAdRectangleImpl(oldObject.getInfluenceArea().getX() + oldObject.getX(),
							oldObject.getInfluenceArea().getY() + oldObject.getY(), oldObject
									.getInfluenceArea().getWidth(), oldObject
									.getInfluenceArea().getHeight()));
		}

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
				new EAdChangeAppearance(newActiveArea, id));
		newActiveAreaReference.addBehavior(
				EAdMouseEventImpl.MOUSE_EXITED,
				new EAdChangeAppearance(newActiveArea, newActiveArea
						.getInitialBundle()));

		// Event to show (or not) the active area
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		EAdConditionEventImpl event = new EAdConditionEventImpl();
		event.setId(newActiveAreaReference.getId() + "_VisibleEvent");
		event.setCondition(condition);

		EAdField<Boolean> visibleField = new EAdFieldImpl<Boolean>(
				newActiveAreaReference, EAdBasicSceneElement.VAR_VISIBLE);

		EAdChangeFieldValueEffect visibleVar = new EAdChangeFieldValueEffect();
		visibleVar.setId(newActiveAreaReference.getId() + "_visibleEffect");
		visibleVar.addField(visibleField);
		BooleanOperation op = new BooleanOperation();
		op.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);
		visibleVar.setOperation(op);
		event.addEffect(ConditionedEventType.CONDITIONS_MET,
				visibleVar);

		EAdChangeFieldValueEffect notVisibleVar = new EAdChangeFieldValueEffect();
		notVisibleVar.setId(newActiveArea.getId() + "_notVisibleEffect");
		notVisibleVar.addField(visibleField);
		op = new BooleanOperation();
		op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
		notVisibleVar.setOperation(op);
		event.addEffect(ConditionedEventType.CONDITIONS_UNMET,
				notVisibleVar);

		newActiveAreaReference.getEvents().add(event);
		return newActiveAreaReference;
	}

}
