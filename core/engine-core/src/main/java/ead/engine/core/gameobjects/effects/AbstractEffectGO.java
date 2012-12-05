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

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scenes.EAdComplexSceneElement;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.ComplexSceneElementGOImpl;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public abstract class AbstractEffectGO<P extends EAdEffect> extends
		ComplexSceneElementGOImpl<EAdComplexSceneElement> implements
		EffectGO<P> {

	/**
	 * The input action
	 */
	protected InputAction<?> action;

	/**
	 * Element that launched the effect
	 */
	protected EAdSceneElement parent;

	/**
	 * If the effect has been stopped
	 */
	private boolean stopped;

	/**
	 * The effect
	 */
	protected P effect;

	/**
	 * The game state
	 */
	protected GameState gameState;

	public AbstractEffectGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState,
				eventFactory);
	}

	public P getEffect() {
		return effect;
	}

	public void setEffect(P effect) {
		this.effect = effect;
	}

	@Override
	public void initialize() {
		stopped = false;
		for (EAdEffect e : effect.getPreviousEffects()) {
			gameState.addEffect(e, action, parent);
		}
	}

	public void update() {
		if (this.isVisualEffect())
			super.update();
	}

	@Override
	public boolean isBlocking() {
		return effect.isBlocking();
	}

	@Override
	public boolean isQueueable() {
		return effect.isQueueable();
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

	public void stop() {
		stopped = true;
	}

	public void finish() {
		stopped = true;
		for (EAdEffect e : effect.getNextEffects()) {
			gameState.addEffect(e, action, parent);
		}
	}

	public void setGUIAction(InputAction<?> action) {
		this.action = action;
	}

	public void setParent(EAdSceneElement parent) {
		this.parent = parent;
	}

	public boolean isVisualEffect() {
		return false;
	}

	public boolean isFinished() {
		return !effect.isQueueable();
	}

}
