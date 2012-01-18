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

package ead.common.importer.subimporters.chapter.scene.elements;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.subimporters.chapter.scene.ShapedElementImporter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEventType;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdFieldImpl;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.predef.effects.MoveActiveElementEf;
import ead.common.params.fills.EAdColor;
import ead.common.params.fills.EAdPaintImpl;
import ead.common.params.text.EAdString;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.Shape;
import ead.common.resources.assets.drawable.basics.enums.Alignment;
import ead.common.util.EAdRectangle;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.Rectangle;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Description;
import es.eucm.eadventure.common.data.chapter.elements.Element;

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

	protected void addGoToExit(SceneElementImpl newExit, Exit oldObject,
			EAdCondition enableCondition, EAdEffect finalEffect) {

		if (factory.isFirstPerson()) {
			newExit.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, finalEffect);
		} else {
			MoveActiveElementEf move = new MoveActiveElementEf();
			move.setTarget(newExit.getDefinition());
			move.getNextEffects().add(finalEffect);
			newExit.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, move);
		}

	}

	protected void addInfluenceArea(EAdSceneElement sceneElement,
			EAdRectangle bounds, InfluenceArea influenceArea) {
		boolean hasInfluenceArea = influenceArea != null
				&& influenceArea.getWidth() != 0
				&& influenceArea.getHeight() != 0;

		EAdRectangle rect = null;
		if (hasInfluenceArea) {
			rect = new EAdRectangle(influenceArea.getX() + bounds.getX(),
					influenceArea.getY() + bounds.getY(),
					influenceArea.getWidth(), influenceArea.getHeight());
		} else {
			rect = new EAdRectangle(bounds.getX() - INFLUENCE_MARGIN,
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

	protected void setShape(SceneElementImpl sceneElement, Rectangle exit) {
		Shape shape = ShapedElementImporter.importShape(exit);
		sceneElement.setPosition(exit.getX(), exit.getY());
		shape.setPaint(EAdColor.TRANSPARENT);

		sceneElement
				.getDefinition()
				.getResources()
				.addAsset(sceneElement.getDefinition().getInitialBundle(),
						SceneElementDefImpl.appearance, shape);
	}

	protected EAdCondition getEnableCondition(Conditions c) {
		EAdCondition condition = conditionsImporter.init(c);
		condition = conditionsImporter.convert(c, condition);

		return condition;

	}

	protected void setDocumentation(SceneElementDefImpl newElement,
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

	protected void addDefaultBehavior(SceneElementImpl sceneElement,
			EAdString shortDescription) {
		sceneElement.setVarInitialValue(SceneElementImpl.VAR_NAME,
				sceneElement.getDefinition().getName());
		if (shortDescription != null) {
			SpeakEf showDescription = new SpeakEf(
					shortDescription);
			showDescription.setAlignment(Alignment.CENTER);
			showDescription.setColor(EAdPaintImpl.WHITE_ON_BLACK,
					EAdColor.TRANSPARENT);
			sceneElement.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK,
					showDescription);
		}
	}

	protected void addVisibleEvent(SceneElementImpl newReference,
			EAdCondition condition) {

		ConditionedEv event = new ConditionedEv();
		event.setCondition(condition);

		EAdField<Boolean> enableField = new EAdFieldImpl<Boolean>(
				newReference, SceneElementImpl.VAR_VISIBLE);

		ChangeFieldEf changeEnable = new ChangeFieldEf();

		changeEnable.addField(enableField);
		changeEnable.setOperation(new BooleanOp(condition));
		event.addEffect(ConditionedEventType.CONDITIONS_MET, changeEnable);
		event.addEffect(ConditionedEventType.CONDITIONS_UNMET, changeEnable);

		newReference.getEvents().add(event);
	}

}
