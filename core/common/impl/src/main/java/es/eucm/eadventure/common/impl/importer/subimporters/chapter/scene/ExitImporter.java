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
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.transitions.EAdTransition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;

public class ExitImporter implements EAdElementImporter<Exit, EAdSceneElement> {

	private static int ID_GENERATOR = 0;
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;
	private EAdElementFactory factory;
	private EffectsImporterFactory effectsImporterFactory;

	@Inject
	public ExitImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory,
			EffectsImporterFactory effectsImporterFactory) {
		this.conditionsImporter = conditionsImporter;
		this.factory = factory;
		this.effectsImporterFactory = effectsImporterFactory;
	}

	public EAdSceneElement init(Exit oldObject) {
		EAdBasicSceneElement newExit = new EAdBasicSceneElement("exit"
				+ ID_GENERATOR++);
		return newExit;
	}

	@Override
	public EAdSceneElement convert(Exit oldObject, Object object) {
		EAdBasicSceneElement newExit = (EAdBasicSceneElement) object;

		Shape shape = ShapedElementImporter.importShape(oldObject, newExit);

		// FIXME deleted when exits display name and icon
		shape.setFill(EAdBorderedColor.BLACK_ON_WHITE);

		newExit.getResources().addAsset(newExit.getInitialBundle(),
				EAdBasicSceneElement.appearance, shape);

		// Event to show (or not) the exit
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		if (oldObject.getInfluenceArea() != null) {
			newExit.setInfluenceArea(new EAdRectangleImpl(oldObject.getInfluenceArea().getX(),
					oldObject.getInfluenceArea().getY(),
					oldObject.getInfluenceArea().getWidth(),
					oldObject.getInfluenceArea().getHeight()));
		}

		for (Effect e : oldObject.getEffects().getEffects()) {
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(condition);
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}

		EAdScene scene = (EAdScene) factory.getElementById(oldObject
				.getNextSceneId());
		EAdChangeScene effect = new EAdChangeScene("change_screen_"
				+ newExit.getId(), scene, EAdTransition.BASIC);
		effect.setCondition(condition);

		newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);

		for (Effect e : oldObject.getPostEffects().getEffects()) {
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(condition);
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}

		boolean hasNotEffects = false;
		for (Effect e : oldObject.getNotEffects().getEffects()) {
			hasNotEffects = true;
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(new NOTCondition(condition));
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}

		if (!hasNotEffects) {
			EAdConditionEventImpl event = new EAdConditionEventImpl(
					newExit.getId() + "_VisibleEvent");
			event.setCondition(condition);

			EAdField<Boolean> visibleField = new EAdFieldImpl<Boolean>(newExit,
					EAdBasicSceneElement.VAR_VISIBLE);

			EAdChangeFieldValueEffect visibleVar = new EAdChangeFieldValueEffect(
					newExit.getId() + "_visibleEffect");
			visibleVar.addVar(visibleField);
			BooleanOperation op = new BooleanOperation("booleanOpTrue");
			op.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);
			visibleVar.setOperation(op);
			event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
					visibleVar);

			EAdChangeFieldValueEffect notVisibleVar = new EAdChangeFieldValueEffect(
					newExit.getId() + "_notVisibleEffect");
			notVisibleVar.addVar(visibleField);
			op = new BooleanOperation("booleanOpFalse");
			op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
			notVisibleVar.setOperation(op);
			event.addEffect(
					EAdConditionEvent.ConditionedEvent.CONDITIONS_UNMET,
					notVisibleVar);

			newExit.getEvents().add(event);
		}

		return newExit;
	}

}
