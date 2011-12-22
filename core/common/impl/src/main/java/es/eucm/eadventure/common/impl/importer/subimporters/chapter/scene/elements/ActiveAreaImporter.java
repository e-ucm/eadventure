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

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ActionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ShapedElementImporter;
import es.eucm.eadventure.common.model.elements.EAdAction;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.effects.ActorActionsEf;
import es.eucm.eadventure.common.model.elements.guievents.MouseEventImpl;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;

public class ActiveAreaImporter extends ElementImporter<ActiveArea> {

	private EAdElementImporter<Action, EAdAction> actionImporter;

	@Inject
	public ActiveAreaImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementImporter<Action, EAdAction> actionImporter,
			StringHandler stringHandler, EAdElementFactory factory) {
		super(factory, conditionsImporter, stringHandler);
		this.actionImporter = actionImporter;
	}

	@Override
	public EAdSceneElement init(ActiveArea oldObject) {
		SceneElementDefImpl newActiveArea = new SceneElementDefImpl();
		EAdSceneElement newActiveAreaReference = new SceneElementImpl(
				newActiveArea);
		return newActiveAreaReference;
	}

	@Override
	public EAdSceneElement convert(ActiveArea oldObject, Object object) {
		// Reference to the active area
		SceneElementImpl newActiveAreaReference = (SceneElementImpl) object;
		newActiveAreaReference.setId(oldObject.getId() + "_ActiveArea");

		SceneElementDefImpl newActiveArea = (SceneElementDefImpl) newActiveAreaReference
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
		addEnableEvent(newActiveAreaReference,
				getEnableCondition(oldObject.getConditions()));

		// Add description
		super.addDefaultBehavior(newActiveAreaReference,
				newActiveArea.getDesc());

		return newActiveAreaReference;
	}

	private void setShape(EAdSceneElementDef newActiveArea, ActiveArea oldObject) {
		Shape shape = ShapedElementImporter.importShape(oldObject);

		newActiveArea.getResources().addAsset(newActiveArea.getInitialBundle(),
				SceneElementDefImpl.appearance, shape);
	}

	private void addActions(ActiveArea oldObject,
			SceneElementDefImpl newActiveArea,
			SceneElementImpl newActiveAreaReference) {

		ActorActionsEf showActions = new ActorActionsEf(
				newActiveArea);
		newActiveArea.addBehavior(MouseEventImpl.MOUSE_RIGHT_CLICK,
				showActions);

		((ActionImporter) actionImporter).addAllActions(oldObject.getActions(),
				newActiveArea, true);

	}

}
