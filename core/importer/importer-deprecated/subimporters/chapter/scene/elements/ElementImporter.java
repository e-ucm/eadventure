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

package ead.importer.subimporters.chapter.scene.elements;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.ConditionedEv;
import es.eucm.ead.model.elements.events.enums.ConditionedEvType;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.predef.effects.MoveActiveElementToMouseEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.GhostElement;
import es.eucm.ead.model.elements.trajectories.NodeTrajectory;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.MouseGEv;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.subimporters.chapter.scene.ShapedElementImporter;
import es.eucm.ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.Rectangle;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;

public abstract class ElementImporter<T> implements
		EAdElementImporter<T, SceneElement> {

	private static final int INFLUENCE_MARGIN = 20;

	protected EAdElementFactory factory;

	protected EAdElementImporter<Conditions, Condition> conditionsImporter;

	protected StringHandler stringHandler;

	protected ImportAnnotator annotator;

	public ElementImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, Condition> conditionsImporter,
			StringHandler stringHandler, ImportAnnotator annotator) {
		this.factory = factory;
		this.conditionsImporter = conditionsImporter;
		this.stringHandler = stringHandler;
		this.annotator = annotator;
	}

	protected void addGoToExit(SceneElement newExit, Exit oldObject,
			Condition enableCondition, Effect finalEffect) {

		if (factory.isFirstPerson()) {
			newExit.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, finalEffect);
		} else {
			MoveActiveElementToMouseEf move = new MoveActiveElementToMouseEf();
			move.setTarget(newExit.getDefinition());
			move.getNextEffects().add(finalEffect);
			newExit.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		}

	}

	protected void addInfluenceArea(SceneElement sceneElement,
			es.eucm.ead.model.params.util.Rectangle bounds,
			InfluenceArea influenceArea) {
		boolean hasInfluenceArea = influenceArea != null
				&& influenceArea.getWidth() != 0
				&& influenceArea.getHeight() != 0;

		es.eucm.ead.model.params.util.Rectangle rect = null;
		if (hasInfluenceArea) {
			rect = new es.eucm.ead.model.params.util.Rectangle(influenceArea
					.getX()
					+ bounds.getX(), influenceArea.getY() + bounds.getY(),
					influenceArea.getWidth(), influenceArea.getHeight());
		} else {
			rect = new es.eucm.ead.model.params.util.Rectangle(bounds.getX()
					- INFLUENCE_MARGIN, bounds.getY() - INFLUENCE_MARGIN,
					bounds.getWidth() + INFLUENCE_MARGIN * 2, bounds
							.getHeight()
							+ INFLUENCE_MARGIN * 2);
		}
		sceneElement.
				.setVarInitialValue(NodeTrajectory.VAR_INFLUENCE_AREA, rect);
	}

	protected void addInfluenceArea(SceneElement sceneElement,
			Rectangle element, InfluenceArea influenceArea) {
		addInfluenceArea(sceneElement,
				ShapedElementImporter.getBounds(element), influenceArea);
	}

	protected void setShape(GhostElement sceneElement, Rectangle exit, Paint p) {
		EAdShape shape = ShapedElementImporter.importShape(exit);
		shape.setPaint(p);
		sceneElement.setPosition(exit.getX(), exit.getY());
		sceneElement.setAppearance(shape);
	}

	protected Condition getEnableCondition(Conditions c) {
		Condition condition = conditionsImporter.init(c);
		condition = conditionsImporter.convert(c, condition);

		return condition;

	}

	protected void addVisibleEvent(SceneElement newReference,
			Condition condition) {

		if (condition.equals(EmptyCond.TRUE)) {
			return;
		}

		ConditionedEv event = new ConditionedEv();
		event.setCondition(condition);

		ElementField enableField = new ElementField(newReference,
				SceneElement.VAR_VISIBLE);

		ChangeFieldEf changeEnable = new ChangeFieldEf();

		changeEnable.addField(enableField);
		changeEnable.setOperation(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, changeEnable);
		event.addEffect(ConditionedEvType.CONDITIONS_UNMET, changeEnable);

		newReference.addEvent(event);
	}

}
