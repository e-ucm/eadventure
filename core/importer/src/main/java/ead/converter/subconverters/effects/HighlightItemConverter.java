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

package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.sceneelements.ChangeColorEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.EAdElementsCache;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.HighlightItemEffect;

public class HighlightItemConverter implements
		EffectConverter<HighlightItemEffect> {

	private EAdElementsCache elementsCache;

	public HighlightItemConverter(EAdElementsCache elementsCache) {
		this.elementsCache = elementsCache;
	}

	@Override
	public List<EAdEffect> convert(HighlightItemEffect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		switch (e.getHighlightType()) {
		case HighlightItemEffect.HIGHLIGHT_BLUE:
			blue = 1.0f;
			break;
		case HighlightItemEffect.HIGHLIGHT_GREEN:
			green = 1.0f;
			break;
		case HighlightItemEffect.HIGHLIGHT_RED:
			red = 1.0f;
			break;
		case HighlightItemEffect.HIGHLIGHT_BORDER:
			// XXX Highlight border
			red = blue = green = 0.5f;
			break;
		}

		ChangeColorEf changeColor = new ChangeColorEf(red, green, blue);
		changeColor.setSceneElement(elementsCache.get(e.getTargetId()));
		list.add(changeColor);

		if (e.isHighlightAnimated()) {
			SceneElementDef def = (SceneElementDef) elementsCache.get(e
					.getTargetId());
			BasicField<EAdSceneElement> sceneElementField = new BasicField<EAdSceneElement>(
					def, SceneElementDef.VAR_SCENE_ELEMENT);
			InterpolationEf interpolation = new InterpolationEf(
					sceneElementField, SceneElement.VAR_SCALE, 1.0f, 0.5f, 1000);
			InterpolationEf interpolation2 = new InterpolationEf(
					sceneElementField, SceneElement.VAR_SCALE, 0.5f, 1.0f, 1000);
			changeColor.getNextEffects().add(interpolation);
			interpolation.getNextEffects().add(interpolation2);
			list.add(interpolation);
			list.add(interpolation2);
		}

		return list;
	}
}
