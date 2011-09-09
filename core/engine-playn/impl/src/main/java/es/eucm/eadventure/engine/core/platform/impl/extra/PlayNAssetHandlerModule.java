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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.SpriteImage;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.SpriteImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.DisplacedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.DisplacedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.SoundImpl;
import es.eucm.eadventure.engine.core.impl.DefaultStringHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineSpriteImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNSound;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeSpriteImage;
import es.eucm.eadventure.engine.core.platform.impl.PlayNAssetHandler;

public class PlayNAssetHandlerModule extends AbstractGinModule {

	@Override
	protected void configure() {
		//TODO Necessary?
		//bind(StringFileHandler.class).to(DefaultStringFileHandler.class);
		bind(StringHandler.class).to(DefaultStringHandler.class);
		bind(AssetHandler.class).to(PlayNAssetHandler.class);
	}

	@Provides
	@Singleton
	Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> provideMap() {
		Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> map = new HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>>();

		map.put(ImageImpl.class, PlayNEngineImage.class);
		map.put(Image.class, PlayNEngineImage.class);
		map.put(Frame.class, PlayNEngineImage.class);
		map.put(Caption.class, PlayNEngineCaption.class);
		map.put(CaptionImpl.class, PlayNEngineCaption.class);
		map.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(ComposedDrawableImpl.class, RuntimeComposedDrawable.class);
		map.put(RectangleShape.class, PlayNBezierShape.class);
		map.put(BezierShape.class, PlayNBezierShape.class);
		map.put(DisplacedDrawable.class, RuntimeDisplacedDrawable.class);
		map.put(DisplacedDrawableImpl.class, RuntimeDisplacedDrawable.class);
		map.put(SpriteImage.class, RuntimeSpriteImage.class);
		map.put(SpriteImageImpl.class, PlayNEngineSpriteImage.class);
		map.put(Sound.class, PlayNSound.class);
		map.put(SoundImpl.class, PlayNSound.class);

		return map;
	}

}
