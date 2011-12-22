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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.StringFileHandler;
import es.eucm.eadventure.common.impl.strings.DefaultStringFileHandler;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.SpriteImage;
import es.eucm.eadventure.common.resources.assets.drawable.basics.SpriteImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.animation.Frame;
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
import es.eucm.eadventure.engine.core.impl.StringHandlerImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineSpriteImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeFilteredDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeSpriteImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.sound.DesktopSound;
import es.eucm.eadventure.engine.core.platform.impl.DesktopAssetHandler;
import es.eucm.eadventure.engine.core.platform.impl.specialassetrenderers.VLCDesktopVideoRenderer;

public class DesktopAssetHandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringFileHandler.class).to(DefaultStringFileHandler.class);
		bind(StringHandler.class).to(StringHandlerImpl.class);
		bind(AssetHandler.class).to(DesktopAssetHandler.class);
		bind(new TypeLiteral<SpecialAssetRenderer<Video, ?>>() {
		}).to(VLCDesktopVideoRenderer.class);
	}

	@SuppressWarnings("unchecked")
	@Provides
	@Singleton
	Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> provideMap() {
		Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> map = new HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>>();

		map.put(ImageImpl.class, DesktopEngineImage.class);
		map.put(Image.class, DesktopEngineImage.class);
		map.put(Caption.class, DesktopEngineCaption.class);
		map.put(CaptionImpl.class, DesktopEngineCaption.class);
		map.put(ComposedDrawable.class, (Class<? extends RuntimeAsset<?>>) RuntimeComposedDrawable.class);
		map.put(ComposedDrawableImpl.class, (Class<? extends RuntimeAsset<?>>) RuntimeComposedDrawable.class);
		map.put(RectangleShape.class, DesktopBezierShape.class);
		map.put(BezierShape.class, DesktopBezierShape.class);
		map.put(DisplacedDrawable.class, (Class<? extends RuntimeAsset<?>>) RuntimeDisplacedDrawable.class);
		map.put(DisplacedDrawableImpl.class, (Class<? extends RuntimeAsset<?>>) RuntimeDisplacedDrawable.class);
		map.put(SpriteImage.class, (Class<? extends RuntimeAsset<?>>) RuntimeSpriteImage.class);
		map.put(SpriteImageImpl.class, DesktopEngineSpriteImage.class);
		map.put(Sound.class, DesktopSound.class);
		map.put(SoundImpl.class, DesktopSound.class);
		map.put(FilteredDrawable.class, (Class<? extends RuntimeAsset<?>>) RuntimeFilteredDrawable.class);
		map.put(FilteredDrawableImpl.class, (Class<? extends RuntimeAsset<?>>) RuntimeFilteredDrawable.class);

		return map;
	}

}
