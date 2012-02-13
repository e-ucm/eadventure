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

package ead.engine.core.platform.extra;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import ead.common.StringFileHandler;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.CaptionImpl;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.basics.SpriteImageImpl;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.ComposedDrawableImpl;
import ead.common.resources.assets.drawable.compounds.DisplacedDrawable;
import ead.common.resources.assets.drawable.compounds.DisplacedDrawableImpl;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawableImpl;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.multimedia.SoundImpl;
import ead.common.resources.assets.multimedia.Video;
import ead.common.strings.DefaultStringFileHandler;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.DesktopAssetHandler;
import ead.engine.core.platform.RuntimeAsset;
import ead.engine.core.platform.SpecialAssetRenderer;
import ead.engine.core.platform.StringHandlerImpl;
import ead.engine.core.platform.assets.DesktopBezierShape;
import ead.engine.core.platform.assets.DesktopEngineImage;
import ead.engine.core.platform.assets.DesktopEngineSpriteImage;
import ead.engine.core.platform.assets.RuntimeCaption;
import ead.engine.core.platform.assets.RuntimeComposedDrawable;
import ead.engine.core.platform.assets.RuntimeDisplacedDrawable;
import ead.engine.core.platform.assets.RuntimeFilteredDrawable;
import ead.engine.core.platform.assets.sound.DesktopSound;
import ead.engine.core.platform.specialassetrenderers.DesktopVideoRenderer;
import java.util.HashMap;
import java.util.Map;

public class DesktopAssetHandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringFileHandler.class).to(DefaultStringFileHandler.class);
		bind(StringHandler.class).to(StringHandlerImpl.class);
		bind(AssetHandler.class).to(DesktopAssetHandler.class);
//		bind(new TypeLiteral<SpecialAssetRenderer<Video, ?>>() {
//		}).to(VLCDesktopVideoRenderer.class);
		bind(new TypeLiteral<SpecialAssetRenderer<Video, ?>>() {
			}).to(DesktopVideoRenderer.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides
	@Singleton
	Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> provideMap() {

		Map map = new HashMap();

		map.put(ImageImpl.class, DesktopEngineImage.class);
		map.put(Caption.class, RuntimeCaption.class);
		map.put(CaptionImpl.class, RuntimeCaption.class);
		map.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(ComposedDrawableImpl.class, RuntimeComposedDrawable.class);
		map.put(RectangleShape.class, DesktopBezierShape.class);
		map.put(BezierShape.class, DesktopBezierShape.class);
		map.put(DisplacedDrawable.class, RuntimeDisplacedDrawable.class);
		map.put(DisplacedDrawableImpl.class, RuntimeDisplacedDrawable.class);
		map.put(SpriteImageImpl.class, DesktopEngineSpriteImage.class);
		map.put(Sound.class, DesktopSound.class);
		map.put(SoundImpl.class, DesktopSound.class);
		map.put(FilteredDrawable.class, RuntimeFilteredDrawable.class);
		map.put(FilteredDrawableImpl.class, RuntimeFilteredDrawable.class);

		return (Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>)map;
	}
}
