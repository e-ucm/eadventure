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

package ead.engine.core.factories.mapproviders;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.NinePatchImage;
import ead.common.model.assets.drawable.basics.animation.FramesAnimation;
import ead.common.model.assets.drawable.basics.shapes.BezierShape;
import ead.common.model.assets.drawable.basics.shapes.CircleShape;
import ead.common.model.assets.drawable.basics.shapes.RectangleShape;
import ead.common.model.assets.drawable.compounds.ComposedDrawable;
import ead.common.model.assets.drawable.compounds.EAdStateDrawable;
import ead.common.model.assets.drawable.compounds.StateDrawable;
import ead.common.model.assets.drawable.filters.EAdFilteredDrawable;
import ead.common.model.assets.drawable.filters.FilteredDrawable;
import ead.common.model.assets.multimedia.Sound;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.engine.core.assets.RuntimeAsset;
import ead.engine.core.assets.drawables.RuntimeCaption;
import ead.engine.core.assets.drawables.RuntimeComposedDrawable;
import ead.engine.core.assets.drawables.RuntimeFilteredDrawable;
import ead.engine.core.assets.drawables.RuntimeFramesAnimation;
import ead.engine.core.assets.drawables.RuntimeImage;
import ead.engine.core.assets.drawables.RuntimeNinePatchImage;
import ead.engine.core.assets.drawables.RuntimeStateDrawable;
import ead.engine.core.assets.drawables.shapes.GdxBezierShape;
import ead.engine.core.assets.drawables.shapes.GdxCircleShape;
import ead.engine.core.assets.drawables.shapes.GdxRectangleShape;
import ead.engine.core.assets.fonts.RuntimeFont;
import ead.engine.core.assets.multimedia.RuntimeSound;

public class AssetHandlerMap
		extends
		AbstractMapProvider<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> {

	public AssetHandlerMap() {
		factoryMap.put(Image.class, RuntimeImage.class);
		factoryMap.put(BezierShape.class, GdxBezierShape.class);
		factoryMap.put(RectangleShape.class, GdxRectangleShape.class);
		factoryMap.put(CircleShape.class, GdxCircleShape.class);
		factoryMap.put(Caption.class, RuntimeCaption.class);
		factoryMap.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		factoryMap.put(BasicFont.class, RuntimeFont.class);
		factoryMap.put(EAdFont.class, RuntimeFont.class);
		factoryMap.put(StateDrawable.class, RuntimeStateDrawable.class);
		factoryMap.put(EAdStateDrawable.class, RuntimeStateDrawable.class);
		factoryMap.put(FramesAnimation.class, RuntimeFramesAnimation.class);
		factoryMap
				.put(EAdFilteredDrawable.class, RuntimeFilteredDrawable.class);
		factoryMap.put(FilteredDrawable.class, RuntimeFilteredDrawable.class);
		factoryMap.put(Sound.class, RuntimeSound.class);
		factoryMap.put(NinePatchImage.class, RuntimeNinePatchImage.class);
	}

}
