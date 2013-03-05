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

package ead.common.model.elements.predef.effects;

import ead.common.model.elements.EAdElement;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.text.EAdString;

public class SpeakSceneElementEf extends SpeakEf {

	public SpeakSceneElementEf(EAdElement element, EAdString text) {
		super(text);
		this.setElement(element);
	}

	public void setElement(EAdElement element) {
		if (element instanceof EAdSceneElement) {
			setOrigin(element);
			setStateField(new BasicField<String>(element,
					SceneElement.VAR_STATE));
		} else if (element instanceof EAdSceneElementDef) {
			BasicField<EAdSceneElement> fieldElement = new BasicField<EAdSceneElement>(
					element, SceneElementDef.VAR_SCENE_ELEMENT);
			setOrigin(fieldElement);
			setStateField(new BasicField<String>(fieldElement,
					SceneElement.VAR_STATE));
		} else if (element != null) {
			setOrigin(element);
		}
	}

	private void setOrigin(EAdElement element) {
		BasicField<Float> centerX = new BasicField<Float>(element,
				SceneElement.VAR_CENTER_X);
		BasicField<Float> centerY = new BasicField<Float>(element,
				SceneElement.VAR_CENTER_Y);

		setPosition(centerX, centerY);

	}

}
