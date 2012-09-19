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

import ead.common.model.elements.scenes.SceneElement;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class BasicSceneElementGO extends SceneElementGOImpl<SceneElement> {

	private EvaluatorFactory evaluatorFactory;

	@Inject
	public BasicSceneElementGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			EvaluatorFactory evaluatorFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
		this.evaluatorFactory = evaluatorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#
	 * getDraggableElement(es.eucm.eadventure.engine.core.MouseState)
	 */
	public SceneElementGO<?> getDraggableElement() {
		if (evaluatorFactory.evaluate(element.getDragCond())) {
			return this;
		}
		return null;
	}

}
