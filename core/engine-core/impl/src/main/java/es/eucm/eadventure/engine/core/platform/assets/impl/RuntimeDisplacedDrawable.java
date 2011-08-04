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

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.DisplacedDrawable;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;

public class RuntimeDisplacedDrawable extends AbstractRuntimeAsset<DisplacedDrawable> implements DrawableAsset<DisplacedDrawable>{

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger("RuntimeComposedDrawable");

	/**
	 * The asset handler
	 */
	protected AssetHandler assetHandler;
	
	@Inject 
	public RuntimeDisplacedDrawable(AssetHandler assetHandler ){
		this.assetHandler = assetHandler;
		logger.info("New instance");
	}

	@Override
	public boolean loadAsset() {
		return assetHandler.getRuntimeAsset(descriptor.getDrawable()).loadAsset();
	}

	@Override
	public void freeMemory() {
		assetHandler.getRuntimeAsset(descriptor.getDrawable()).freeMemory();
	}

	@Override
	public boolean isLoaded() {
		return assetHandler.getRuntimeAsset(descriptor.getDrawable()).isLoaded();
	}

	@Override
	public void update(GameState state) {
		assetHandler.getRuntimeAsset(descriptor.getDrawable()).update(state);
	}

	@Override
	public int getWidth() {
		return ((DrawableAsset<?>) assetHandler.getRuntimeAsset(descriptor.getDrawable())).getWidth();
	}

	@Override
	public int getHeight() {
		return ((DrawableAsset<?>) assetHandler.getRuntimeAsset(descriptor.getDrawable())).getHeight();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S> getDrawable() {
		return (DrawableAsset<S>) this;
	}

	public AssetDescriptor getDrawableAsset() {
		return descriptor.getDrawable();
	}

	public EAdPosition getDisplacement() {
		return descriptor.getDisplacement();
	}

}
