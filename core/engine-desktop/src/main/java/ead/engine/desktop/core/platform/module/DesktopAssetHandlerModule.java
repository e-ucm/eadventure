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

package ead.engine.desktop.core.platform.module;

import java.util.Map;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import ead.common.StringFileHandler;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.multimedia.EAdSound;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.common.strings.DefaultStringFileHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.core.platform.assets.SpecialAssetRenderer;
import ead.engine.desktop.core.platform.DesktopAssetHandler;
import ead.engine.desktop.core.platform.assets.DesktopFont;
import ead.engine.desktop.core.platform.assets.drawables.basics.DesktopBezierShape;
import ead.engine.desktop.core.platform.assets.drawables.basics.DesktopImage;
import ead.engine.desktop.core.platform.assets.multimedia.DesktopSound;
import ead.engine.desktop.core.platform.assets.specialassetrenderers.DesktopVideoRenderer;
import ead.engine.desktop.core.platform.assets.specialassetrenderers.VLCDesktopVideoRenderer;
import ead.engine.java.core.platform.modules.AssetHandlerModule;

public class DesktopAssetHandlerModule extends AssetHandlerModule {

	private static final boolean USE_JVLC = false;

	@Override
	protected void configure() {
		bind(StringFileHandler.class).to(DefaultStringFileHandler.class);
		bind(AssetHandler.class).to(DesktopAssetHandler.class);
		if (USE_JVLC) {
			bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
			}).to(VLCDesktopVideoRenderer.class);
		} else {
			bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
			}).to(DesktopVideoRenderer.class);
		}
		super.configure();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides
	@Singleton
	public Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> provideMap() {
		Map map = super.provideMap();
		map.put(Image.class, DesktopImage.class);
		map.put(BezierShape.class, DesktopBezierShape.class);
		map.put(EAdSound.class, DesktopSound.class);
		map.put(Sound.class, DesktopSound.class);
		map.put(BasicFont.class, DesktopFont.class);
		map.put(EAdFont.class, DesktopFont.class);
		return (Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>) map;
	}
}
