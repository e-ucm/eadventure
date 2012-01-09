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

package ead.common.importer.subimporters.chapter.scene;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.EAdElementImporter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEventType;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdFieldImpl;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;

@Singleton
public class BarrierImporter implements
		EAdElementImporter<Barrier, EAdSceneElement> {

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	@Inject
	public BarrierImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter) {
		this.conditionsImporter = conditionsImporter;
	}

	@Override
	public EAdSceneElement init(Barrier oldObject) {
		EAdSceneElement element = new SceneElementImpl();
		element.setId(oldObject.getId());
		return element;
	}

	@Override
	public EAdSceneElement convert(Barrier oldObject, Object newElement) {
		SceneElementImpl barrier = (SceneElementImpl) newElement;

		if (oldObject.getConditions() != null) {
			EAdCondition condition = conditionsImporter.init(oldObject
					.getConditions());
			condition = conditionsImporter.convert(oldObject.getConditions(),
					condition);
			ConditionedEv event = new ConditionedEv();
			event.setId("barrierCondition");
			event.setCondition(condition);
			EAdField<Boolean> barrierOn = new EAdFieldImpl<Boolean>(barrier,
					NodeTrajectoryDefinition.VAR_BARRIER_ON);
			event.addEffect(ConditionedEventType.CONDITIONS_MET,
					new ChangeFieldEf(barrierOn,
							BooleanOp.TRUE_OP));
			event.addEffect(ConditionedEventType.CONDITIONS_UNMET,
					new ChangeFieldEf(barrierOn,
							BooleanOp.FALSE_OP));

			barrier.getEvents().add(event);
		}

		RectangleShape rectangle = new RectangleShape( oldObject.getWidth(), oldObject.getHeight() );
		barrier.getDefinition()
				.getResources()
				.addAsset(barrier.getDefinition().getInitialBundle(),
						SceneElementDefImpl.appearance, rectangle);
		barrier.setPosition(new EAdPosition(Corner.TOP_LEFT, oldObject
				.getX(), oldObject.getY()));

		return barrier;
	}

}
