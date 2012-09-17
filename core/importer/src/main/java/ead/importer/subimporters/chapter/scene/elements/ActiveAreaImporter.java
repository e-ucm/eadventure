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

import com.google.inject.Inject;

import ead.common.model.elements.EAdAction;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.importer.EAdElementImporter;
import ead.importer.EAdventureImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.subimporters.chapter.ActionImporter;
import ead.importer.subimporters.chapter.scene.ShapedElementImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;

public class ActiveAreaImporter extends ElementImporter<ActiveArea> {

	private EAdElementImporter<Action, EAdAction> actionImporter;

	@Inject
	public ActiveAreaImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementImporter<Action, EAdAction> actionImporter,
			StringHandler stringHandler, EAdElementFactory factory,
			ImportAnnotator annotator) {
		super(factory, conditionsImporter, stringHandler, annotator);
		this.actionImporter = actionImporter;
	}

	@Override
	public EAdSceneElement init(ActiveArea oldObject) {
		SceneElementDef newActiveArea = new SceneElementDef();
		EAdSceneElement newActiveAreaReference = new SceneElement(
				newActiveArea);
		return newActiveAreaReference;
	}

	@Override
	public EAdSceneElement convert(ActiveArea oldObject, Object object) {
		// Reference to the active area
		SceneElement newActiveAreaReference = (SceneElement) object;
		newActiveAreaReference.getDefinition().setId(oldObject.getId() + "_ActiveArea");
		newActiveAreaReference.setId(oldObject.getId() + "_ActiveArea_ref");

		SceneElementDef newActiveArea = (SceneElementDef) newActiveAreaReference
				.getDefinition();

		// add actions
		addActions(oldObject, newActiveArea, newActiveAreaReference);

		// set documentation
		setDocumentation(newActiveArea, oldObject);

		// set shape
		setShape(newActiveArea, oldObject);

		// set influence area
		addInfluenceArea(newActiveAreaReference, oldObject,
				oldObject.getInfluenceArea());

		// enable event
		addVisibleEvent(newActiveAreaReference,
				getEnableCondition(oldObject.getConditions()));

		// Add description
		super.addDefaultBehavior(newActiveAreaReference,
				newActiveArea.getDesc());

		return newActiveAreaReference;
	}

	private void setShape(EAdSceneElementDef newActiveArea, ActiveArea oldObject) {
		EAdShape shape = ShapedElementImporter.importShape(oldObject);
		if ( EAdventureImporter.IMPORTER_DEBUG ){
			ColorFill c = new ColorFill( ColorFill.GREEN.toString() );
			c.setAlpha(100);
			shape.setPaint(c);
		}
		else {
			shape.setPaint(Paint.TRANSPARENT);
		}

		newActiveArea.getResources().addAsset(newActiveArea.getInitialBundle(),
				SceneElementDef.appearance, shape);
	}

	private void addActions(ActiveArea oldObject,
			SceneElementDef newActiveArea,
			SceneElement newActiveAreaReference) {

		ActorActionsEf showActions = new ActorActionsEf(
				newActiveArea);
		newActiveArea.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK,
				showActions);

		((ActionImporter) actionImporter).addAllActions(oldObject.getActions(),
				newActiveArea, true);

	}

}
