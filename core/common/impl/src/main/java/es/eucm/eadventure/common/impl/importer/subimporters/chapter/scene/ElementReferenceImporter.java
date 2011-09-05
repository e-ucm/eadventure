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
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdSystemEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSystemEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;

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
			EAdElementFactory elementFactory) {
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

		newRef.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, oldObject.getX(),
				oldObject.getY()));
		newRef.setScale(oldObject.getScale());
		newRef.setInitialOrientation(Orientation.S);
		EAdBasicActor actor = (EAdBasicActor) factory.getElementById(oldObject
				.getTargetId());
		newRef.setReferencedActor(actor);

		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		EAdConditionEventImpl visibilityEvent = new EAdConditionEventImpl(
				"visibilityCondition", condition);
		EAdChangeFieldValueEffect visibilityEffect = new EAdChangeFieldValueEffect(
				"visibilityConditionEffect", new EAdFieldImpl<Boolean>(newRef,
						EAdBasicSceneElement.VAR_VISIBLE),
				new BooleanOperation("", condition));
		visibilityEvent.addEffect(
				EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
				visibilityEffect);
		visibilityEvent.addEffect(
				EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET,
				visibilityEffect);
		newRef.getEvents().add(visibilityEvent);

		EAdSystemEventImpl startVisibilityEvent = new EAdSystemEventImpl(
				"startVisibilityEvent");
		startVisibilityEvent.addEffect(EAdSystemEvent.Event.GAME_LOADED,
				visibilityEffect);
		elementFactory.getCurrentChapterModel().getEvents()
				.add(startVisibilityEvent);

		if (actor.getActions().size() != 0) {
			EAdActorActionsEffect showActions = new EAdActorActionsEffect(
					actor.getId() + "_showActions", newRef);
			newRef.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
		}

		if (oldObject.getInfluenceArea() != null) {
			int x = oldObject.getInfluenceArea().getX();
			int y = oldObject.getInfluenceArea().getY();
			int width = oldObject.getInfluenceArea().getWidth();
			int height = oldObject.getInfluenceArea().getHeight();
			EAdRectangleImpl r = new EAdRectangleImpl(x, y, width, height);
			newRef.setInfluenceArea(r);
		}

		return newRef;
	}
}
