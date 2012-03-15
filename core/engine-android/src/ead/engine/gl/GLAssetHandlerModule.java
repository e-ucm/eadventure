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

package ead.engine.gl;

import java.util.Map;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.multimedia.EAdSound;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.engine.core.platform.assets.AndroidFont;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.core.platform.assets.SpecialAssetRenderer;
import ead.engine.core.platform.assets.multimedia.AndroidSound;
import ead.engine.core.platform.assets.specialassetrenderers.AndroidVideoRenderer;
import ead.engine.core.platform.modules.AssetHandlerModule;

public class GLAssetHandlerModule extends AssetHandlerModule {
	@Override
	protected void configure() {
		super.configure();
		bind(AssetHandler.class).to(GLAssetHandler.class);
		// Bind to AndroidVideoRenderer (uses API mediaplayer) or
		// RockPlayerAndroidVideoRenderer
		bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
		}).to(AndroidVideoRenderer.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Provides
	@Singleton
	public Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> provideMap() {
		Map map = super.provideMap();
		map.put(Image.class, GLImage.class);
		map.put(RectangleShape.class, GLBezierShape.class);
		map.put(BezierShape.class, GLBezierShape.class);
		map.put(EAdSound.class, AndroidSound.class);
		map.put(Sound.class, AndroidSound.class);
		map.put(EAdFont.class, AndroidFont.class);
		map.put(BasicFont.class, AndroidFont.class);
		return (Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>) map;
	}
}
