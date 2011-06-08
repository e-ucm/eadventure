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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.BundledDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;

/**
 * Represents a runtime bundled anmation, base on a {@link BundledAnimation}
 * 
 */
public class RuntimeBundledAnimation extends AbstractRuntimeAsset<BundledDrawable> implements DrawableAsset<BundledDrawable> {

	/**
	 * Current state for the animation
	 */
	private String currentState;

	private AssetHandler assetHandler;

	private Map<String, DrawableAsset<?>> states;

	@Inject
	public RuntimeBundledAnimation(AssetHandler handler) {
		this.assetHandler = handler;
		states = new HashMap<String, DrawableAsset<?>>();
	}

	/**
	 * Current drawable
	 */
	private DrawableAsset<?> currentDrawable;

	/**
	 * Sets current state
	 * 
	 * @param currentState
	 *            the new state
	 */
	public void setState(String currentState) {
		this.currentState = currentState;
	}

	@Override
	public boolean loadAsset() {
		if (!states.containsKey(currentState)) {
			AssetDescriptor a = descriptor.getDrawable(currentState);
			currentDrawable = (DrawableAsset<?>) assetHandler
					.getRuntimeAsset(a);
			states.put(currentState, currentDrawable);

		}
		return true;
	}

	@Override
	public void freeMemory() {
		for (String s : states.keySet()) {
			states.get(s).freeMemory();
		}

	}

	@Override
	public boolean isLoaded() {
		return states.get(currentState) != null;
	}

	@Override
	public void update(GameState state) {
		if ( currentDrawable != null )
			currentDrawable.update(state);
	}

	@Override
	public int getWidth() {
		if (currentDrawable != null)
			return currentDrawable.getWidth();
		return 1;
	}

	@Override
	public int getHeight() {
		if (currentDrawable != null)
			return currentDrawable.getHeight();
		return 1;
	}

	@Override
	public <S extends Drawable> DrawableAsset<S> getDrawable() {
		if ( !isLoaded()){
			loadAsset();
		}
		
		currentDrawable = states.get(currentState);
		return currentDrawable.getDrawable();
	}

}
