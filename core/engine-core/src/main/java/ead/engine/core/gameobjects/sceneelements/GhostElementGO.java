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

package ead.engine.core.gameobjects.sceneelements;

import com.google.inject.Inject;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.shapes.RectangleShape;
import ead.common.model.elements.scenes.EAdGhostElement;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.fills.Paint;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class GhostElementGO extends SceneElementGOImpl {

	private RuntimeDrawable<?, ?> interactionArea;

	private EAdGhostElement ghostElement;

	@Inject
	public GhostElementGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
	}

	@Override
	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		this.ghostElement = (EAdGhostElement) element;
		EAdDrawable interactionAreaDrawable = ghostElement.getInteractionArea();

		if (interactionAreaDrawable != null) {
			interactionArea = assetHandler
					.getDrawableAsset(interactionAreaDrawable);
			RectangleShape area = new RectangleShape(
					interactionArea.getWidth(), interactionArea.getHeight(),
					Paint.TRANSPARENT);
			ghostElement.getDefinition().setAppearance(area);
		}
	}

	@Override
	public boolean contains(int x, int y) {
		return ghostElement.isCatchAll()
				|| (interactionArea != null && isVisible() && interactionArea
						.contains(x, y));
	}

}
