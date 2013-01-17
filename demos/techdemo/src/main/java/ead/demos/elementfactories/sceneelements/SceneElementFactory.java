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

package ead.demos.elementfactories.sceneelements;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.util.EAdPosition;
import ead.demos.elementfactories.EAdElementsFactory;

public class SceneElementFactory {

	/**
	 * Creates an scene element with the given appearance in the given
	 * coordinates
	 * 
	 * @param appearance
	 * @param x
	 * @param y
	 * @return
	 */
	public SceneElement createSceneElement(EAdDrawable appearance, int x, int y) {
		SceneElement sceneElement = new SceneElement(appearance);
		sceneElement.setPosition(new EAdPosition(x, y));
		return sceneElement;
	}

	/**
	 * Creates an scene element which its appearance changes from appearance1 to
	 * appearance2 when the mouse enters in it
	 * 
	 * @param appearance1
	 * @param appearance2
	 * @param x
	 * @param y
	 * @return
	 */
	public SceneElement createSceneElement(EAdDrawable appearance1,
			EAdDrawable appearance2, int x, int y) {
		SceneElement sceneElement = createSceneElement(appearance1, x, y);
		String bundle = "bundle2";
		sceneElement.getDefinition().addAsset(bundle,
				SceneElementDef.appearance, appearance2);
		sceneElement.addBehavior(MouseGEv.MOUSE_ENTERED, EAdElementsFactory
				.getInstance().getEffectFactory().getChangeAppearance(
						sceneElement, bundle));
		sceneElement.addBehavior(MouseGEv.MOUSE_EXITED, EAdElementsFactory
				.getInstance().getEffectFactory().getChangeAppearance(
						sceneElement, SceneElementDef.INITIAL_BUNDLE));
		return sceneElement;
	}

	/**
	 * Creates an scene element with the givena appearance which launches the
	 * given effect when right clicked
	 * 
	 * @param appearance
	 * @param effect
	 * @return
	 */
	public SceneElement createSceneElement(EAdDrawable appearance, int x,
			int y, EAdEffect effect) {
		SceneElement sceneElement = this.createSceneElement(appearance, x, y);
		sceneElement.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
		return sceneElement;
	}

	/**
	 * Creates an scene element with a text as appearance in the given position
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @return
	 */
	public SceneElement createSceneElement(String text, int x, int y) {
		return createSceneElement(EAdElementsFactory.getInstance()
				.getCaptionFactory().createCaption(text), x, y);
	}

	public SceneElement createSceneElement(String string, int x, int y,
			EAdEffect effect) {
		return createSceneElement(EAdElementsFactory.getInstance()
				.getCaptionFactory().createCaption(string), x, y, effect);
	}

}
