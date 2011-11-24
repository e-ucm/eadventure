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
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdSystemEvent;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSystemEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;

/**
 * Elements reference importer
 * 
 */
public class ElementReferenceImporter implements
		EAdElementImporter<ElementReference, EAdSceneElement> {

	private EAdElementFactory factory;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	@Inject
	public ElementReferenceImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter) {
		this.factory = factory;
		this.conditionsImporter = conditionsImporter;
	}

	/**
	 * Used to ensure unique identifiers in references
	 */
	private static int ID_GENERATOR = 0;

	public EAdSceneElement init(ElementReference oldObject) {
		EAdBasicSceneElement newRef = new EAdBasicSceneElement();
		newRef.setId(oldObject.getTargetId() + "_reference_" + ID_GENERATOR++);
		return newRef;
	}

	@Override
	public EAdSceneElement convert(ElementReference oldObject, Object object) {

		EAdSceneElementDefImpl actor = (EAdSceneElementDefImpl) factory
				.getElementById(oldObject.getTargetId());
		EAdBasicSceneElement newRef = (EAdBasicSceneElement) object;

		newRef.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, oldObject.getX(),
				oldObject.getY()));
		newRef.setScale(oldObject.getScale());
		newRef.setInitialOrientation(Orientation.S);

		newRef.setDefinition(actor);
		if (oldObject.getInfluenceArea() != null) {
			newRef.setVarInitialValue(
					NodeTrajectoryDefinition.VAR_INFLUENCE_AREA,
					new EAdRectangleImpl(oldObject.getInfluenceArea().getX(),
							oldObject.getInfluenceArea().getY(), oldObject
									.getInfluenceArea().getWidth(), oldObject
									.getInfluenceArea().getHeight()));
		}

		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		EAdConditionEventImpl visibilityEvent = new EAdConditionEventImpl(condition);
		visibilityEvent.setId("visibilityCondition");
		EAdChangeFieldValueEffect visibilityEffect = new EAdChangeFieldValueEffect(
				 new EAdFieldImpl<Boolean>(newRef,
						EAdBasicSceneElement.VAR_VISIBLE),
				new BooleanOperation(condition));
		visibilityEffect.setId("visibilityConditionEffect");
		visibilityEvent.addEffect(
				ConditionedEventType.CONDITIONS_MET,
				visibilityEffect);
		visibilityEvent.addEffect(
				ConditionedEventType.CONDITIONS_UNMET,
				visibilityEffect);
		newRef.getEvents().add(visibilityEvent);

		EAdSystemEventImpl startVisibilityEvent = new EAdSystemEventImpl();
		startVisibilityEvent.setId("startVisibilityEvent");
		startVisibilityEvent.addEffect(EAdSystemEvent.Event.GAME_LOADED,
				visibilityEffect);
		// TODO what is this for?
		// elementFactory.getCurrentChapterModel().getEvents()
		// .add(startVisibilityEvent);

		EAdActorActionsEffect showActions = new EAdActorActionsEffect(newRef);
		showActions.setId(actor.getId() + "_showActions");
		newRef.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);

		if (oldObject.getInfluenceArea() != null) {
			int x = oldObject.getInfluenceArea().getX();
			int y = oldObject.getInfluenceArea().getY();
			int width = oldObject.getInfluenceArea().getWidth();
			int height = oldObject.getInfluenceArea().getHeight();
			EAdRectangleImpl r = new EAdRectangleImpl(x, y, width, height);
			newRef.setVarInitialValue(
					NodeTrajectoryDefinition.VAR_INFLUENCE_AREA, r);
		}

		return newRef;
	}
}
