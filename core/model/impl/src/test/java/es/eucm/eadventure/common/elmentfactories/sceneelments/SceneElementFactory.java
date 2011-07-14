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

package es.eucm.eadventure.common.elmentfactories.sceneelments;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public class SceneElementFactory {

	private static int ID_GENERATOR = 0;

	/**
	 * Creates an scene element with the given appearance in the given
	 * coordinates
	 * 
	 * @param appearance
	 * @param x
	 * @param y
	 * @return
	 */
	public EAdBasicSceneElement createSceneElement(AssetDescriptor appearance,
			int x, int y) {
		EAdBasicSceneElement sceneElement = new EAdBasicSceneElement(
				"sceneElement" + ID_GENERATOR++);
		sceneElement.getResources().addAsset(sceneElement.getInitialBundle(),
				EAdBasicSceneElement.appearance, appearance);
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
	public EAdBasicSceneElement createSceneElement(AssetDescriptor appearance1,
			AssetDescriptor appearance2, int x, int y) {
		EAdBasicSceneElement sceneElement = createSceneElement(appearance1, x,
				y);
		EAdBundleId bundle = new EAdBundleId("bundle2");
		sceneElement.getResources().addBundle(bundle);
		sceneElement.getResources().addAsset(bundle,
				EAdBasicSceneElement.appearance, appearance2);
		sceneElement.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED,
				EAdElementsFactory.getInstance().getEffectFactory()
						.getChangeAppearance(sceneElement, bundle));
		sceneElement.addBehavior(
				EAdMouseEventImpl.MOUSE_EXITED,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeAppearance(sceneElement,
								sceneElement.getInitialBundle()));
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
	public EAdBasicSceneElement createSceneElement(AssetDescriptor appearance,
			int x, int y, EAdEffect effect) {
		EAdBasicSceneElement sceneElement = this.createSceneElement(appearance,
				x, y);
		sceneElement.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
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
	public EAdBasicSceneElement createSceneElement(String text, int x, int y) {
		return createSceneElement(EAdElementsFactory.getInstance()
				.getCaptionFactory().createCaption(text), x, y);
	}

	public EAdBasicSceneElement createSceneElement(String string, int x, int y,
			EAdEffect effect) {
		return createSceneElement(EAdElementsFactory.getInstance()
				.getCaptionFactory().createCaption(string), x, y, effect);
	}

}
