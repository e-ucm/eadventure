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

package ead.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.elements.effects.timedevents.ShowSceneElementEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.util.Position;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.PlayAnimationEffect;

public class PlayAnimationEffectImporter extends
		EffectImporter<PlayAnimationEffect, ShowSceneElementEf> {

	private ResourceImporter resourceImporter;

	@Inject
	public PlayAnimationEffectImporter(
			EAdElementImporter<Conditions, Condition> conditionImporter,
			ResourceImporter resourceImporter, ImportAnnotator annotator) {
		super(conditionImporter, annotator);
		this.resourceImporter = resourceImporter;
	}

	@Override
	public ShowSceneElementEf init(PlayAnimationEffect oldObject) {
		ShowSceneElementEf showScene = new ShowSceneElementEf();
		return showScene;
	}

	@Override
	public ShowSceneElementEf convert(PlayAnimationEffect oldObject,
			Object newElement) {
		ShowSceneElementEf effect = super.convert(oldObject, newElement);

		EAdDrawable asset = (EAdDrawable) resourceImporter.getAssetDescritptor(
				oldObject.getPath(), Image.class);
		SceneElement element = new SceneElement(asset);
		element.setPosition(new Position(oldObject.getX(), oldObject.getY()));
		if (asset instanceof FramesAnimation) {
			int time = 0;
			for (int i = 0; i < ((FramesAnimation) asset).getFrameCount(); i++) {
				time += ((FramesAnimation) asset).getFrame(i).getTime();
			}
			effect.setTime(time);
		} else {
			effect.setTime(1000);
		}

		effect.setSceneElement(element);
		return effect;
	}

}
