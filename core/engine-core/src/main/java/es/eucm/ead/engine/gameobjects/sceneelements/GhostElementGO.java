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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.GhostElement;

public class GhostElementGO extends SceneElementGO {

	private boolean catchAll;

	public boolean ghostVisible;

	@Inject
	public GhostElementGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, Game game,
			EventFactory eventFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
	}

	public void setElement(EAdSceneElement e) {
		super.setElement(e);
		catchAll = ((GhostElement) e).isCatchAll();
		ghostVisible = false;
	}

	@Override
	public void drawChildren(SpriteBatch batch, float parentAlpha) {
		if (ghostVisible) {
			super.drawChildren(batch, parentAlpha);
		}
	}

	public void setInteractionAreaVisible(boolean visible) {
		this.ghostVisible = visible;
	}

	public Actor hit(float x, float y, boolean touchable) {
		if (catchAll && touchable) {
			return this;
		} else {
			return super.hit(x, y, touchable);
		}
	}

	public boolean contains(float x, float y) {
		return catchAll ? true : super.contains(x, y);
	}

}
