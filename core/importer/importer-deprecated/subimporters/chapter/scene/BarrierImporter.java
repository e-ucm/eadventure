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

package ead.importer.subimporters.chapter.scene;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.ConditionedEv;
import es.eucm.ead.model.elements.events.enums.ConditionedEvType;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.elements.trajectories.NodeTrajectory;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;
import ead.importer.EAdElementImporter;
import ead.importer.EAdventureImporter;
import ead.importer.annotation.ImportAnnotator;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;

@Singleton
public class BarrierImporter implements
		EAdElementImporter<Barrier, SceneElement> {

	private EAdElementImporter<Conditions, Condition> conditionsImporter;

	protected ImportAnnotator annotator;

	@Inject
	public BarrierImporter(
			EAdElementImporter<Conditions, Condition> conditionsImporter,
			ImportAnnotator annotator) {
		this.conditionsImporter = conditionsImporter;
		this.annotator = annotator;
	}

	@Override
	public SceneElement init(Barrier oldObject) {
		SceneElement element = new SceneElement();
		return element;
	}

	@Override
	public SceneElement convert(Barrier oldObject, Object newElement) {
		SceneElement barrier = (SceneElement) newElement;

		if (oldObject.getConditions() != null) {
			Condition condition = conditionsImporter.init(oldObject
					.getConditions());
			condition = conditionsImporter.convert(oldObject.getConditions(),
					condition);
			ConditionedEv event = new ConditionedEv();
			event.setCondition(condition);
			ElementField<Boolean> barrierOn = new ElementField<Boolean>(barrier,
					NodeTrajectory.VAR_BARRIER_ON);
			event.addEffect(ConditionedEvType.CONDITIONS_MET,
					new ChangeFieldEf(barrierOn, EmptyCond.TRUE));
			event.addEffect(ConditionedEvType.CONDITIONS_UNMET,
					new ChangeFieldEf(barrierOn, EmptyCond.FALSE));

			barrier.addEvent(event);
		}

		RectangleShape rectangle = new RectangleShape(oldObject.getWidth(),
				oldObject.getHeight());
		if (EAdventureImporter.IMPORTER_DEBUG) {
			ColorFill c = new ColorFill(ColorFill.YELLOW.toString());
			c.setAlpha(100);
			rectangle.setPaint(c);
		} else {
			rectangle.setPaint(Paint.TRANSPARENT);
		}
		barrier.getDefinition().addAsset(SceneElementDef.appearance, rectangle);
		barrier.setPosition(new Position(Corner.TOP_LEFT, oldObject.getX(),
				oldObject.getY()));

		return barrier;
	}

}
