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

package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.PlayAnimationEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

public class PlayAnimationEffectImporter extends
		EffectImporter<PlayAnimationEffect, EAdShowSceneElement> {

	private ResourceImporter resourceImporter;

	@Inject
	public PlayAnimationEffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			ResourceImporter resourceImporter) {
		super(conditionImporter);
		this.resourceImporter = resourceImporter;
	}

	@Override
	public EAdShowSceneElement init(PlayAnimationEffect oldObject) {
		EAdShowSceneElement showScene = new EAdShowSceneElement();
		showScene.setId("playAnimationEffect" + ID_GENERATOR++);
		return showScene;
	}

	@Override
	public EAdShowSceneElement convert(PlayAnimationEffect oldObject,
			Object newElement) {
		EAdShowSceneElement effect = super.convert(oldObject, newElement);


		Drawable asset = (Drawable) resourceImporter.getAssetDescritptor(
				oldObject.getPath(), ImageImpl.class);
		EAdBasicSceneElement element = new EAdBasicSceneElement(asset);
		element.setId("animation" + ID_GENERATOR++);
		element.setPosition(new EAdPositionImpl(oldObject.getX(), oldObject
				.getY()));
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
