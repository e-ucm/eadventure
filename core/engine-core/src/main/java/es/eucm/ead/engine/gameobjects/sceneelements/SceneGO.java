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

package es.eucm.ead.engine.gameobjects.sceneelements;

import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;

import java.util.Comparator;

public class SceneGO extends GroupElementGO implements
		Comparator<SceneElementGO> {

	@Inject
	public SceneGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, Game game,
			EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
		setComparator(this);
	}

	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		EAdSceneElement bg = ((EAdScene) element).getBackground();
		if (bg != null) {
			SceneElementGO bgGO = sceneElementFactory.get(bg);
			addSceneElement(bgGO);
			bgGO.setZ(Integer.MIN_VALUE / 2);
		}
	}

	@Override
	public int compare(SceneElementGO o1, SceneElementGO o2) {
		return (int) Math.signum(o1.getZ() - o2.getZ());
	}

	public boolean getReturnable() {
		return ((EAdScene) element).getReturnable();
	}

}
