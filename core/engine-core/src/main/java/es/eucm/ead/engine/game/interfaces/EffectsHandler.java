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

package es.eucm.ead.engine.game.interfaces;

import com.badlogic.gdx.scenes.scene2d.Event;
import es.eucm.ead.engine.factories.EffectFactory;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EffectsHandler {

	private static Logger logger = LoggerFactory.getLogger("EffectsHandler");

	/**
	 * Effects factory
	 */
	private EffectFactory effectFactory;

	/**
	 * Game state
	 */
	private GameState gameState;

	/**
	 * A list with the current effects
	 */
	private List<EffectGO<?>> effects;

	/**
	 * Auxiliary variable, to avoid new every loop
	 */
	private ArrayList<EffectGO<?>> finishedEffects;

	public EffectsHandler(GameState gameState, EffectFactory effectFactory) {
		this.effectFactory = effectFactory;
		this.gameState = gameState;
		effects = new ArrayList<EffectGO<?>>();
		finishedEffects = new ArrayList<EffectGO<?>>();
	}

	/**
	 * Returns a list with all game objects linked to the current effects.
	 *
	 * @return a list with all game objects linked to the current effects.
	 */
	public List<EffectGO<?>> getEffects() {
		return effects;
	}

	/**
	 * Adds a new effect to the effects' tail
	 *
	 * @param e      the new effect
	 * @param action the action that launched the effect
	 * @param parent scene element who launched the effect
	 * @return the effect game object create from the effect element
	 */
	public EffectGO<?> addEffect(EAdEffect e, Event action,
			EAdSceneElement parent) {
		if (e != null) {
			if (gameState.evaluate(e.getCondition())) {
				logger.debug("{} launched", e);
				EffectGO<?> effectGO = effectFactory.get(e);
				if (effectGO == null) {
					logger.warn("No game object for effect {}", e.getClass());
					return null;
				}
				effectGO.setGUIAction(action);
				effectGO.setParent(parent);
				effectGO.initialize();
				if (effectGO.isQueueable() && !effectGO.isFinished()) {
					effects.add(effectGO);
				} else {
					effectGO.finish();
					effectFactory.remove(effectGO);
				}
				return effectGO;
			} else if (e.isNextEffectsAlways()) {
				logger.debug("{} discarded. But next effects launched", e);
				for (EAdEffect ne : e.getNextEffects())
					addEffect(ne, null, null);
			} else {
				logger.debug("{} discarded", e);
			}
		}
		return null;
	}

	/**
	 * Clears all the current effects
	 *
	 * @param persistent sets if persistent effects should also be deleted
	 */
	public void clearEffects(boolean persistent) {
		for (EffectGO<?> effect : this.getEffects()) {
			if (!effect.getElement().isPersistent() || persistent) {
				effect.stop();
			}
		}
		effects.clear();
		logger.debug("Effects cleared");
	}

	public void act(float delta) {
		// Effects
		finishedEffects.clear();
		boolean block = false;
		int i = 0;
		while (i < getEffects().size()) {
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
			effects.remove(e);
			effectFactory.remove(e);

		}
	}
}
