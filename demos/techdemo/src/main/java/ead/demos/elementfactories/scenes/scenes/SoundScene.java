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

package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.elements.EAdEffect;
import ead.common.params.fills.Paint;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.text.BasicFont;
import ead.common.util.EAdURI;
import ead.demos.elementfactories.EAdElementsFactory;

public class SoundScene extends EmptyScene {

	public SoundScene() {
		Caption caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption(
						"Play",
						Paint.WHITE_ON_BLACK,
						Paint.BLACK_ON_WHITE,
						new BasicFont(new EAdURI("@binary/DroidSans-Bold.ttf"),
								20));
		EAdEffect effect = EAdElementsFactory.getInstance().getEffectFactory()
				.getPlaySound("@binary/sound.mp3");
		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(caption, 10, 10, effect));
	}

	@Override
	public String getSceneDescription() {
		return "A scene where a sound is played";
	}

	public String getDemoName() {
		return "Sound Scene";
	}
}
