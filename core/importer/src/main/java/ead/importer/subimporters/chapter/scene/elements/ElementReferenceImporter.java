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

import java.awt.Dimension;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.DragEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.Position;
import ead.common.model.params.util.Rectangle;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.NPC;

/**
 * Elements reference importer
 * 
 */
public class ElementReferenceImporter extends ElementImporter<ElementReference> {

	private ResourceImporter resourceImporter;

	@Inject
	public ElementReferenceImporter(EAdElementFactory factory,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			StringHandler stringHandler, ResourceImporter resourceImporter,
			ImportAnnotator annotator) {
		super(factory, conditionsImporter, stringHandler, annotator);
		this.resourceImporter = resourceImporter;
	}

	public EAdSceneElement init(ElementReference oldObject) {
		SceneElement newRef = new SceneElement();
		return newRef;
	}

	@Override
	public EAdSceneElement convert(ElementReference oldObject, Object object) {

		SceneElementDef actor = (SceneElementDef) factory
				.getElementById(oldObject.getTargetId());
		SceneElement newRef = (SceneElement) object;

		newRef.setPosition(new Position(Position.Corner.BOTTOM_CENTER,
				oldObject.getX(), oldObject.getY()));
		newRef.setInitialScale(oldObject.getScale());
		newRef.setInitialOrientation(Orientation.S);
		newRef.setDefinition(actor);

		if (factory.getCurrentOldChapterModel().getAtrezzo(
				oldObject.getTargetId()) == null) {

			if (!factory.isFirstPerson()) {
				// add influence area
				String imageUri = getImageUri(oldObject.getTargetId());
				Dimension d = resourceImporter
						.getDimensionsForOldImage(imageUri);
				int width = (int) d.getWidth();
				int height = (int) d.getHeight();
				Position p = new Position(oldObject.getX(), oldObject.getY(),
						0.5f, 1.0f);
				float scale = oldObject.getScale();
				Rectangle bounds = new Rectangle(p.getJavaX(width * scale), p
						.getJavaY(height * scale), (int) (width * scale),
						(int) (height * scale));
				super.addInfluenceArea(newRef, bounds, oldObject
						.getInfluenceArea());
			}

			// add description
			// super.addDefaultBehavior(newRef,
			// newRef.getDefinition().getDesc());

			// add dragable
			if (factory.isDraggableActor(actor)) {
				newRef.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, new DragEf());

			}
		} else {
			newRef.setVarInitialValue(SceneElement.VAR_ENABLE, Boolean.FALSE);
		}

		// add visible
		super.addVisibleEvent(newRef, super.getEnableCondition(oldObject
				.getConditions()));

		return newRef;
	}

	private boolean isReturnWhenDragged(String targetId) {
		Item i = factory.getCurrentOldChapterModel().getItem(targetId);
		NPC npc = factory.getCurrentOldChapterModel().getCharacter(targetId);
		if (i != null) {
			return i.isReturnsWhenDragged();
		} else if (npc != null) {
			// Yeah, I thought too that the right code is the commented line,
			// but
			// no...
			// return npc.isReturnsWhenDragged();
			return false;
		}

		return false;
	}

	private String getImageUri(String targetId) {
		Item i = factory.getCurrentOldChapterModel().getItem(targetId);
		NPC npc = factory.getCurrentOldChapterModel().getCharacter(targetId);
		if (i != null) {
			return i.getResources().get(0).getAssetPath(
					Item.RESOURCE_TYPE_IMAGE);
		} else if (npc != null) {
			return npc.getResources().get(0).getAssetPath(
					NPC.RESOURCE_TYPE_STAND_DOWN);
		}
		return null;
	}
}
