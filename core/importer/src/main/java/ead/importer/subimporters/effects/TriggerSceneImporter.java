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

package ead.importer.subimporters.effects;

import com.google.inject.Inject;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;

public class TriggerSceneImporter extends
		EffectImporter<TriggerSceneEffect, ChangeSceneEf> {

	private EAdElementFactory factory;

	@Inject
	public TriggerSceneImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory, ImportAnnotator annotator) {
		super(conditionImporter, annotator);
		this.factory = factory;
	}

	@Override
	public ChangeSceneEf init(TriggerSceneEffect oldObject) {
		ChangeSceneEf effect = new ChangeSceneEf();
		effect.setId("triggerCutscene");
		return effect;
	}

	@Override
	public ChangeSceneEf convert(TriggerSceneEffect oldObject, Object object) {
		ChangeSceneEf changeScene = super.convert(oldObject, object);

		EAdScene scene = (EAdScene) factory.getElementById(oldObject
				.getTargetId());
		changeScene.setNextScene(scene);

		return changeScene;
	}

	public static EAdTransition getTransition(int type, int time) {
		EAdTransition transition = null;
		switch (type) {
		case Exit.NO_TRANSITION:
			transition = EmptyTransition.instance();
			break;
		case Exit.FADE_IN:
			transition = new FadeInTransition(time);
			break;
		case Exit.LEFT_TO_RIGHT:
			transition = new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
			break;
		case Exit.RIGHT_TO_LEFT:
			transition = new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, false);
			break;
		case Exit.TOP_TO_BOTTOM:
			transition = new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
			break;
		case Exit.BOTTOM_TO_TOP:
			transition = new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, false);
			break;
		default:
			transition = EmptyTransition.instance();
		}
		return transition;
	}

}
