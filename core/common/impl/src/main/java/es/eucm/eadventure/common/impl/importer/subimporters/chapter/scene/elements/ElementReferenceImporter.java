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
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.interfaces.features.enums.Orientation;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.resources.StringHandler;

/**
 * Elements reference importer
 * 
 */
public class ElementReferenceImporter extends ElementImporter<ElementReference> {

	@Inject
	public ElementReferenceImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			StringHandler stringHandler) {
		super(factory, conditionsImporter, stringHandler);
	}

	public EAdSceneElement init(ElementReference oldObject) {
		EAdBasicSceneElement newRef = new EAdBasicSceneElement();
		return newRef;
	}

	@Override
	public EAdSceneElement convert(ElementReference oldObject, Object object) {

		EAdSceneElementDefImpl actor = (EAdSceneElementDefImpl) factory
				.getElementById(oldObject.getTargetId());
		EAdBasicSceneElement newRef = (EAdBasicSceneElement) object;

		newRef.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, oldObject.getX(),
				oldObject.getY()));
		newRef.setScale(oldObject.getScale());
		newRef.setInitialOrientation(Orientation.S);
		newRef.setDefinition(actor);

		// add influence area
		// To do this right, we should load the image representing the item or
		// NPC an check their dimensions.
		// Sounds easy, but it's not, so, width and height are 100. I hope it's
		// enough
		int width = 100;
		int height = 100;
		EAdRectangleImpl bounds = new EAdRectangleImpl(oldObject.getX(),
				oldObject.getY(), (int) (width * oldObject.getScale()),
				(int) (height * oldObject.getScale()));
		super.addInfluenceArea(newRef, bounds, oldObject.getInfluenceArea());

		// add actions
		EAdActorActionsEffect showActions = new EAdActorActionsEffect(newRef);
		newRef.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);

		// add description
		super.addDefaultBehavior(newRef, stringHandler.getString(newRef
				.getDefinition().getDescription()));

		// add enable
		super.addEnableEvent(newRef,
				super.getEnableCondition(oldObject.getConditions()));

		return newRef;
	}
}
