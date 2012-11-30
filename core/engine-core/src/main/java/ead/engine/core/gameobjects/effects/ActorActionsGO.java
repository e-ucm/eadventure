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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.enums.ChangeActorActions;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.platform.GUI;

public class ActorActionsGO extends AbstractEffectGO<ActorActionsEf> {

	private static final Logger logger = LoggerFactory
			.getLogger("ActorActionsGO");

	@Inject
	public ActorActionsGO(SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(gameObjectFactory, gui, gameState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		super.initialize();
		if (element.getChange() == ChangeActorActions.SHOW_ACTIONS) {
			EAdSceneElementDef ref = element.getActionElement();
			if (ref != null) {
				EAdList<EAdSceneElementDef> list = gameState
						.getValueMap().getValue(ref,
								ActorActionsEf.VAR_ACTIONS);
				if (list != null) {
					ActionsHUD hud = getActionsHUD();
					if (hud != null) {
						int x = gameState.getValueMap().getValue(
								SystemFields.MOUSE_X);
						int y = gameState.getValueMap().getValue(
								SystemFields.MOUSE_Y);
						hud.showActions( list, x, y );
					}

				}
			}
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

	private ActionsHUD getActionsHUD() {
		for (HudGO h : gui.getHUDs()) {
			if (h instanceof ActionsHUD) {
				return (ActionsHUD) h;
			}
		}
		logger.warn("There is no Actions HUD. Actions won't be shown");
		return null;
	}
}
