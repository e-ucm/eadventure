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

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.StateDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.DisplacedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.SpriteImage;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.StateDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ComposedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.DisplacedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.common.resources.impl.DefaultStringHandler;
import es.eucm.eadventure.engine.AndroidAssetHandler;
import es.eucm.eadventure.engine.assets.AndroidBezierShape;
import es.eucm.eadventure.engine.assets.AndroidEngineCaption;
import es.eucm.eadventure.engine.assets.AndroidEngineImage;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeBundledAnimation;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeFramesAnimation;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeSpriteImage;

public class AndroidAssetHandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(DefaultStringHandler.class);
		bind(AssetHandler.class).to(AndroidAssetHandler.class);
	}
	
	@Provides
	@Singleton
	Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> provideMap() {
		Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> map = new HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>>( );

		map.put(FramesAnimation.class, RuntimeFramesAnimation.class);
		map.put(ImageImpl.class, AndroidEngineImage.class);
		map.put(Image.class, AndroidEngineImage.class);
		map.put(Frame.class, AndroidEngineImage.class);
		map.put(Caption.class, AndroidEngineCaption.class);
		map.put(CaptionImpl.class, AndroidEngineCaption.class);
		map.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(ComposedDrawableImpl.class, RuntimeComposedDrawable.class);
		map.put(RectangleShape.class, AndroidBezierShape.class);
		map.put(BezierShape.class, AndroidBezierShape.class);
		map.put(StateDrawableImpl.class, RuntimeBundledAnimation.class);
		map.put(StateDrawable.class, RuntimeBundledAnimation.class);
		map.put(DisplacedDrawable.class, RuntimeDisplacedDrawable.class);
		map.put(DisplacedDrawableImpl.class, RuntimeDisplacedDrawable.class);
		map.put(SpriteImage.class, RuntimeSpriteImage.class);
		//TODO Sprite image
		//map.put(SpriteImageImpl.class, AndroidEngineSpriteImage.class);
		
		return map;
	}

	
	
}
