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

package ead.engine.core.platform;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import playn.core.Canvas;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.resources.StringHandler;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.EAdEngine;
import ead.engine.core.game.VariableMap;
import ead.engine.core.platform.AbstractAssetHandler;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.RuntimeAsset;
import ead.engine.core.platform.assets.PlayNBezierShape;
import ead.engine.core.platform.assets.PlayNEngineCaption;
import ead.engine.core.platform.assets.PlayNEngineImage;
import ead.engine.core.platform.assets.PlayNEngineSpriteImage;
import ead.engine.core.platform.assets.PlayNSound;
import ead.engine.core.platform.assets.RuntimeComposedDrawable;
import ead.engine.core.platform.assets.RuntimeDisplacedDrawable;
import ead.engine.core.platform.assets.RuntimeFilteredDrawable;

@Singleton
public class PlayNAssetHandler extends AbstractAssetHandler {

	private EAdEngine engine;

	private FontHandler fontHandler;

	private VariableMap valueMap;

	private StringHandler stringHandler;

	private Logger logger = Logger.getLogger("PlayNAssetHandler");

	@Inject
	public PlayNAssetHandler(
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap,
			FontHandler fontCache, VariableMap valueMap,
			StringHandler stringHandler) {
		super(classMap, fontCache);
		this.fontHandler = fontCache;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
	}

	public void setEngine(EAdEngine engine) {
		this.engine = engine;
	}

	@Override
	public void initilize() {
		// TODO Auto-generated method stub

		setLoaded(true);

	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAbsolutePath(String uri) {
		return uri.replaceAll("@", "eadengine/");
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		if (clazz == PlayNEngineImage.class)
			return new PlayNEngineImage(this);
		if (clazz == PlayNBezierShape.class)
			return new PlayNBezierShape(engine);
		if (clazz == PlayNEngineCaption.class)
			return new PlayNEngineCaption(fontHandler, valueMap, stringHandler,
					this);
		if (clazz == PlayNEngineSpriteImage.class)
			return new PlayNEngineSpriteImage(this);
		if (clazz == PlayNEngineSpriteImage.class)
			return new PlayNEngineSpriteImage(this);
		if (clazz == PlayNSound.class)
			return new PlayNSound(this);
		if ( clazz == RuntimeComposedDrawable.class )
			return new RuntimeComposedDrawable<Canvas>( this );
		if ( clazz == RuntimeDisplacedDrawable.class )
			return new RuntimeDisplacedDrawable<Canvas>( this );
		if ( clazz == RuntimeFilteredDrawable.class )
			return new RuntimeFilteredDrawable<Canvas>( this );

		logger.log(Level.SEVERE, "No instance for runtime asset: " + clazz);
		return null;
	}

}
