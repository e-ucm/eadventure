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

package es.eucm.eadventure.engine.extra;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.SpriteImage;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.DisplacedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.DisplacedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.filters.FilteredDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.filters.FilteredDrawableImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;
import es.eucm.eadventure.common.resources.assets.multimedia.SoundImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.assets.AndroidBezierShape;
import es.eucm.eadventure.engine.assets.AndroidEngineCaption;
import es.eucm.eadventure.engine.assets.AndroidEngineImage;
import es.eucm.eadventure.engine.assets.AndroidSound;
import es.eucm.eadventure.engine.assets.specialassetrenderers.AndroidVideoRenderer;
import es.eucm.eadventure.engine.core.impl.StringHandlerImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;
import es.eucm.eadventure.engine.core.platform.assets.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.RuntimeFilteredDrawable;
import es.eucm.eadventure.engine.core.platform.assets.RuntimeSpriteImage;

public class AndroidAssetHandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(StringHandlerImpl.class);
		bind(AssetHandler.class).to(AndroidAssetHandler.class);
		
		//Bind to AndroidVideoRenderer (uses API mediaplayer) or RockPlayerAndroidVideoRenderer
		bind(new TypeLiteral<SpecialAssetRenderer<Video, ?>>(){}).to(AndroidVideoRenderer.class);
	}
	
	@SuppressWarnings("unchecked")
	@Provides
	@Singleton
	Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> provideMap() {
		Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> map = new HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>>( );
		map.put(ImageImpl.class, AndroidEngineImage.class);
		map.put(Image.class, AndroidEngineImage.class);
		map.put(Caption.class, (Class<? extends RuntimeAsset<?>>) AndroidEngineCaption.class);
		map.put(CaptionImpl.class, (Class<? extends RuntimeAsset<?>>) AndroidEngineCaption.class);
		map.put(ComposedDrawable.class, (Class<? extends RuntimeAsset<?>>) RuntimeComposedDrawable.class);
		map.put(ComposedDrawableImpl.class, (Class<? extends RuntimeAsset<?>>) RuntimeComposedDrawable.class);
		map.put(RectangleShape.class, AndroidBezierShape.class);
		map.put(BezierShape.class, AndroidBezierShape.class);
		map.put(DisplacedDrawable.class, (Class<? extends RuntimeAsset<?>>) RuntimeDisplacedDrawable.class);
		map.put(DisplacedDrawableImpl.class, (Class<? extends RuntimeAsset<?>>) RuntimeDisplacedDrawable.class);
		map.put(SpriteImage.class, (Class<? extends RuntimeAsset<?>>) RuntimeSpriteImage.class);
		map.put(FilteredDrawable.class, (Class<? extends RuntimeAsset<?>>) RuntimeFilteredDrawable.class);
		map.put(FilteredDrawableImpl.class, (Class<? extends RuntimeAsset<?>>) RuntimeFilteredDrawable.class);
		map.put(Sound.class, AndroidSound.class);
		map.put(SoundImpl.class, AndroidSound.class);
		//TODO Sprite image
		//map.put(SpriteImageImpl.class, AndroidEngineSpriteImage.class);
		
		return map;
	}

	
	
}
