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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import playn.core.Canvas;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.StringHandler;
import ead.engine.core.EAdEngine;
import ead.engine.core.game.VariableMap;
import ead.engine.core.platform.assets.PlayNFont;
import ead.engine.core.platform.assets.drawable.basics.PlayNBezierShape;
import ead.engine.core.platform.assets.drawable.basics.PlayNImage;
import ead.engine.core.platform.assets.drawables.basics.RuntimeCaption;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeComposedDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeDisplacedDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeFilteredDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeSpriteImage;
import ead.engine.core.platform.assets.multimedia.PlayNSound;

@Singleton
public class PlayNAssetHandler extends AbstractAssetHandler {

	private Logger logger = LoggerFactory.getLogger("PlayNAssetHandler");

	private EAdEngine engine;

	private FontHandler fontHandler;

	private VariableMap valueMap;

	private StringHandler stringHandler;

	private GUI gui;

	@Inject
	public PlayNAssetHandler(
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> classMap,
			FontHandler fontCache, VariableMap valueMap,
			StringHandler stringHandler, GUI gui) {
		super(classMap, fontCache);
		this.fontHandler = fontCache;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
		this.gui = gui;
	}

	public void setEngine(EAdEngine engine) {
		this.engine = engine;
	}

	@Override
	public void initialize() {
		setLoaded(true);
	}

	@Override
	public void terminate() {

	}

	@Override
	public String getAbsolutePath(String uri) {
		return uri.replaceAll("@", "eadengine/");
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {

		// FIXME: it is ugly to discard all these generics; find another way to
		// get clean builds
		@SuppressWarnings("rawtypes")
		RuntimeAsset r = null;
		if (clazz == PlayNImage.class)
			r = new PlayNImage(this);
		else if (clazz == PlayNBezierShape.class)
			r = new PlayNBezierShape(engine);
		else if (clazz == (Object) RuntimeCaption.class)
			r = new RuntimeCaption<Canvas>(gui, fontHandler, valueMap,
					stringHandler, this);
		else if (clazz == (Object) RuntimeSpriteImage.class)
			r = new RuntimeSpriteImage<Canvas>(this);
		else if (clazz == PlayNSound.class)
			r = new PlayNSound(this);
		else if (clazz == (Object) RuntimeComposedDrawable.class)
			r = new RuntimeComposedDrawable<Canvas>(this);
		else if (clazz == (Object) RuntimeDisplacedDrawable.class)
			r = new RuntimeDisplacedDrawable<Canvas>(this);
		else if (clazz == (Object) RuntimeFilteredDrawable.class)
			r = new RuntimeFilteredDrawable<Canvas>(this);
		else if ( clazz == (Object)PlayNFont.class){
			r = new PlayNFont( this );
		}
		else {
			logger.error("No instance for runtime asset: {}", clazz);
		}

		return (RuntimeAsset<?>) r;
	}

}
