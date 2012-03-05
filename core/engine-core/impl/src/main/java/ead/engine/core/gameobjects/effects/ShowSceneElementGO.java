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

package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.timedevents.ShowSceneElementEf;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;

public class ShowSceneElementGO extends AbstractEffectGO<ShowSceneElementEf> {

	private SceneElementGO<?> sceneElement;
	
	private int time;

	@Inject
	public ShowSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
	}

	@Override
	public void doLayout(EAdTransformation t) {
		gui.addElement(sceneElement, t);
	}

	@Override
	public void initialize() {
		super.initialize();
		time = element.getTime();
		sceneElement = sceneElementFactory.get(element.getSceneElement());

	}

	@Override
	public boolean isVisualEffect() {
		return true;
	}

	@Override
	public boolean isFinished() {
		return time <= 0;
	}

	public void update() {
		super.update();
		sceneElement.update();
		time -= gui.getSkippedMilliseconds();

	}

}
