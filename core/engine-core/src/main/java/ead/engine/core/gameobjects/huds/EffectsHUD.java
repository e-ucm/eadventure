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

package ead.engine.core.gameobjects.huds;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.assets.AssetHandler;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.effects.EffectGO;

/**
 * <p>
 * Default implementation of {@link EffectsHUD}
 * </p>
 * 
 */
@Singleton
public class EffectsHUD extends AbstractHUD {

	public static final String ID = "EffectsHUD";

	/**
	 * List of current {@link EffectGO}
	 */
	private List<EffectGO<?>> effects;

	// Auxiliary variable, to avoid new every loop
	private ArrayList<EffectGO<?>> finishedEffects;

	@Inject
	public EffectsHUD(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(ID, assetHandler, gameObjectFactory, gui, gameState,
				eventFactory, 10);
	}

	//	@Override
	//	public void init() {
	//		finishedEffects = new ArrayList<EffectGO<?>>();
	//		this.setElement(new GroupElement());
	//	}

	public void act(float delta) {
		if (!gameState.isPaused()) {
			effects = gameState.getEffects();
			finishedEffects.clear();
			boolean block = false;
			int i = 0;
			while (i < gameState.getEffects().size()) {
				EffectGO<?> effectGO = effects.get(i);
				i++;

				if (block)
					continue;

				if (effectGO.isStopped() || effectGO.isFinished()) {
					finishedEffects.add(effectGO);
					if (effectGO.isFinished())
						effectGO.finish();
				} else {
					if (effectGO.isBlocking())
						// If effect is blocking, get out of the loop
						block = true;

					effectGO.act(delta);
				}

			}

			// Delete finished effects
			for (EffectGO<?> e : finishedEffects) {
				// logger.info("Finished or discarded effect {}", e.getClass());
				gameState.getEffects().remove(e);
			}
			super.act(delta);
		}
	}

}
