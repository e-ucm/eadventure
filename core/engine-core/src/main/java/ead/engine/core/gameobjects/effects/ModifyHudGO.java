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

import ead.common.model.elements.effects.hud.ModifyHUDEf;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.effects.sceneelement.SceneElementEffectGO;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.huds.TopBasicHUDImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.StringHandler;

public class ModifyHudGO extends SceneElementEffectGO<ModifyHUDEf> {

	private TopBasicHUDImpl basicHUD;

	@Inject
	public ModifyHudGO(AssetHandler assetHandler, StringHandler stringsReader,
			SceneElementGOFactory sceneElementGOFactory, GUI gui,
			GameState gameState) {
		super(assetHandler, sceneElementGOFactory, gui, gameState);
		this.basicHUD = null;
	}

	public void initialize() {
		super.initialize();
		SceneElementGO<?> drawable = this.sceneElementFactory.get(sceneElement);
		if (element.getAdd()) {
			if (!basicHUD.getContainedDrawables().contains(drawable))
				basicHUD.getContainedDrawables().add(drawable);
		} else {
			basicHUD.getContainedDrawables().remove(drawable);
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
