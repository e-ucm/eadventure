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

package ead.common.widgets.containers;

import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.util.EAdPosition.Corner;

public class ColumnContainer extends ComplexSceneElement {

	private EAdSceneElement lastAdded;
	
	public ColumnContainer( ){
		this.setId("columnContainer");
		this.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
	}

	public void add(EAdSceneElement element) {
		element.setPosition(Corner.TOP_LEFT, 0, 0);
		if (lastAdded != null) {
			BasicField<Integer> fieldBottom = new BasicField<Integer>(
					lastAdded, SceneElement.VAR_BOTTOM);
			BasicField<Integer> fieldY = new BasicField<Integer>(element,
					SceneElement.VAR_Y);

			SceneElementEv event = new SceneElementEv();
			ChangeFieldEf updateField = new ChangeFieldEf(fieldY, fieldBottom);
			event.addEffect(SceneElementEvType.ALWAYS, updateField);
			element.getEvents().add(event);
		}
		getSceneElements().add(element);
		lastAdded = element;
	}

}
