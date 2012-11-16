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
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.subimporters.chapter.ActorImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;

public class ActiveAreaImporter extends ElementImporter<ActiveArea> {

	private EAdElementImporter<Action, EAdAction> actionImporter;
	private static Paint ACTIVE_AREA_PAINT = new Paint(new ColorFill(0, 255, 0,
			100), ColorFill.GREEN);

	private ResourceImporter resourceImporter;

	@Inject
	public ActiveAreaImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementImporter<Action, EAdAction> actionImporter,
			StringHandler stringHandler, EAdElementFactory factory,
			ImportAnnotator annotator, ResourceImporter resourceImporter) {
		super(factory, conditionsImporter, stringHandler, annotator);
		this.actionImporter = actionImporter;
		this.resourceImporter = resourceImporter;
	}

	@Override
	public EAdSceneElement init(ActiveArea oldObject) {
		GhostElement newActiveAreaReference = new GhostElement();
		return newActiveAreaReference;
	}

	@Override
	public EAdSceneElement convert(ActiveArea oldObject, Object object) {
		// Reference to the active area
		GhostElement newActiveAreaReference = (GhostElement) object;

		SceneElementDef newActiveArea = (SceneElementDef) newActiveAreaReference
				.getDefinition();

		// set documentation
		EAdEffect[] sounds = ActorImporter.setDocumentation(resourceImporter,
				conditionsImporter, stringHandler,
				oldObject.getDocumentation(), oldObject.getDescriptions(),
				newActiveAreaReference.getDefinition());

		// add actions
		ActorImporter.addDefaultBehavior(stringHandler, factory,
				actionImporter, oldObject.getActions(), newActiveArea, sounds);

		// set shape
		setShape(newActiveAreaReference, oldObject, ACTIVE_AREA_PAINT);

		// set influence area
		addInfluenceArea(newActiveAreaReference, oldObject,
				oldObject.getInfluenceArea());

		// enable event
		addVisibleEvent(newActiveAreaReference,
				getEnableCondition(oldObject.getConditions()));

		return newActiveAreaReference;
	}
}
