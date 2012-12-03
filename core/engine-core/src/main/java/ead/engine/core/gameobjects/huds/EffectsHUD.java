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

import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;

/**
 * <p>
 * Default implementation of {@link EffectsHUD}
 * </p>
 * 
 */
@Singleton
public class EffectsHUD extends AbstractHUD {

	/**
	 * List of current {@link EffectGO}
	 */
	private List<EffectGO<?>> effects;

	// Auxiliary variable, to avoid new every time
	private ArrayList<EffectGO<?>> finishedEffects;

	@Inject
	public EffectsHUD(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory, 10);
	}

	@Override
	public void init() {
		finishedEffects = new ArrayList<EffectGO<?>>();
		this.setElement(new ComplexSceneElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#doLayout()
	 */
	@Override
	public void doLayout(EAdTransformation transformation) {
		int i = 0;
		boolean block = false;
		while (i < effects.size() && !block) {
			EffectGO<?> e = effects.get(i);
			if (e.isVisualEffect()) {
				gui.addElement(e, transformation);
			}
			block = e.isBlocking();
			i++;
		}
	}

	public DrawableGO<?> processAction(InputAction<?> action) {
		int i = 0;
		DrawableGO<?> go = null;
		while (!action.isConsumed() && i < gameState.getEffects().size()) {
			go = gameState.getEffects().get(i).processAction(action);
			i++;
		}
		return go;
	}

	public void update() {
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

					effectGO.update();
				}

			}

			// Delete finished effects
			for (EffectGO<?> e : finishedEffects) {
				// logger.info("Finished or discarded effect {}", e.getClass());
				gameState.getEffects().remove(e);
			}

			boolean visualEffect = false;
			int index = 0;
			while (!visualEffect && index < gameState.getEffects().size()) {
				visualEffect = gameState.getEffects().get(index++)
						.isVisualEffect();
			}
			setVisible(visualEffect);
			super.update();
		}
	}

	public boolean contains(int x, int y) {
		boolean contains = false;
		int i = 0;
		while (!contains && i < gameState.getEffects().size()) {
			contains = gameState.getEffects().get(i).contains(x, y);
			i++;
		}
		return contains;
	}

}
