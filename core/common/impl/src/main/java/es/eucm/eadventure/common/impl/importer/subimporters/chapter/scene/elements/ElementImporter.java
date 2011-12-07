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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.elements;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.Rectangle;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Description;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ShapedElementImporter;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
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
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.enums.Alignment;

public abstract class ElementImporter<T> implements
		EAdElementImporter<T, EAdSceneElement> {

	private static final int INFLUENCE_MARGIN = 20;

	protected EAdElementFactory factory;

	protected EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	protected StringHandler stringHandler;

	public ElementImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			StringHandler stringHandler) {
		this.factory = factory;
		this.conditionsImporter = conditionsImporter;
		this.stringHandler = stringHandler;
	}

	protected void addGoToExit(EAdBasicSceneElement newExit, Exit oldObject,
			EAdCondition enableCondition, EAdEffect finalEffect) {

		if (factory.isFirstPerson()) {
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, finalEffect);
		} else {
			EAdMoveActiveElement move = new EAdMoveActiveElement();
			move.setTarget(newExit.getDefinition());
			move.getNextEffects().add(finalEffect);
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);
		}

	}

	protected void addInfluenceArea(EAdSceneElement sceneElement,
			EAdRectangle bounds, InfluenceArea influenceArea) {
		boolean hasInfluenceArea = influenceArea != null
				&& influenceArea.getWidth() != 0
				&& influenceArea.getHeight() != 0;

		EAdRectangle rect = null;
		if (hasInfluenceArea) {
			rect = new EAdRectangleImpl(influenceArea.getX() + bounds.getX(),
					influenceArea.getY() + bounds.getY(),
					influenceArea.getWidth(), influenceArea.getHeight());
		} else {
			rect = new EAdRectangleImpl(bounds.getX() - INFLUENCE_MARGIN,
					bounds.getY() - INFLUENCE_MARGIN, bounds.getWidth()
							+ INFLUENCE_MARGIN * 2, bounds.getHeight()
							+ INFLUENCE_MARGIN * 2);
		}
		sceneElement.setVarInitialValue(
				NodeTrajectoryDefinition.VAR_INFLUENCE_AREA, rect);
	}

	protected void addInfluenceArea(EAdSceneElement sceneElement,
			Rectangle element, InfluenceArea influenceArea) {
		addInfluenceArea(sceneElement,
				ShapedElementImporter.getBounds(element), influenceArea);
	}

	protected void setShape(EAdBasicSceneElement sceneElement, Rectangle exit) {
		Shape shape = ShapedElementImporter.importShape(exit);
		sceneElement.setPosition(exit.getX(), exit.getY());
		shape.setPaint(EAdColor.TRANSPARENT);

		sceneElement
				.getDefinition()
				.getResources()
				.addAsset(sceneElement.getDefinition().getInitialBundle(),
						EAdSceneElementDefImpl.appearance, shape);
	}

	protected EAdCondition getEnableCondition(Conditions c) {
		EAdCondition condition = conditionsImporter.init(c);
		condition = conditionsImporter.convert(c, condition);

		return condition;

	}

	protected void setDocumentation(EAdSceneElementDefImpl newElement,
			Element oldObject) {
		// FIXME multiple descriptions not supported
		if (oldObject.getDescriptions().size() > 0) {
			Description desc = oldObject.getDescription(0);
			stringHandler.setString(newElement.getName(), desc.getName());
			stringHandler
					.setString(newElement.getDesc(), desc.getDescription());
			stringHandler.setString(newElement.getDetailDesc(),
					desc.getDetailedDescription());
			stringHandler.setString(newElement.getDoc(),
					oldObject.getDocumentation());
			newElement.setId(oldObject.getId() + "_element");
		}

	}

	protected void addDefaultBehavior(EAdBasicSceneElement sceneElement,
			EAdString shortDescription) {
		sceneElement.setVarInitialValue(EAdBasicSceneElement.VAR_NAME,
				sceneElement.getDefinition().getName());
		if (shortDescription != null) {
			EAdSpeakEffect showDescription = new EAdSpeakEffect(
					shortDescription);
			showDescription.setAlignment(Alignment.CENTER);
			showDescription.setColor(EAdPaintImpl.WHITE_ON_BLACK,
					EAdColor.TRANSPARENT);
			sceneElement.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
					showDescription);
		}
	}

	protected void addEnableEvent(EAdBasicSceneElement newActiveAreaReference,
			EAdCondition condition) {

		EAdConditionEventImpl event = new EAdConditionEventImpl();
		event.setCondition(condition);

		EAdField<Boolean> enableField = new EAdFieldImpl<Boolean>(
				newActiveAreaReference, EAdBasicSceneElement.VAR_VISIBLE);

		EAdChangeFieldValueEffect changeEnable = new EAdChangeFieldValueEffect();

		changeEnable.addField(enableField);
		changeEnable.setOperation(new BooleanOperation(condition));
		event.addEffect(ConditionedEventType.CONDITIONS_MET, changeEnable);
		event.addEffect(ConditionedEventType.CONDITIONS_UNMET, changeEnable);

		newActiveAreaReference.getEvents().add(event);
	}

}
