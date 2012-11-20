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

package ead.engine.core.gdx.assets;

import java.util.HashMap;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdStateDrawable;
import ead.common.resources.assets.drawable.compounds.StateDrawable;
import ead.common.resources.assets.drawable.filters.EAdFilteredDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.engine.core.gdx.assets.drawables.GdxBezierShape;
import ead.engine.core.gdx.assets.drawables.GdxCircleShape;
import ead.engine.core.gdx.assets.drawables.GdxImage;
import ead.engine.core.gdx.assets.drawables.GdxRectangleShape;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.core.platform.assets.drawables.basics.RuntimeCaption;
import ead.engine.core.platform.assets.drawables.basics.RuntimeFramesAnimation;
import ead.engine.core.platform.assets.drawables.compounds.RuntimeComposedDrawable;
import ead.engine.core.platform.assets.drawables.compounds.RuntimeFilteredDrawable;
import ead.engine.core.platform.assets.drawables.compounds.RuntimeStateDrawable;

public class GdxAssetHandlerMap
		extends
		HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> {
	private static final long serialVersionUID = 5284553649611577802L;

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public GdxAssetHandlerMap() {
		HashMap inner = new HashMap();
		inner.put(Image.class, GdxImage.class);
		inner.put(BezierShape.class, GdxBezierShape.class);
		inner.put(RectangleShape.class, GdxRectangleShape.class);
		inner.put(CircleShape.class, GdxCircleShape.class);
		inner.put(Caption.class, RuntimeCaption.class);
		inner.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		inner.put(BasicFont.class, GdxFont.class);
		inner.put(EAdFont.class, GdxFont.class);
		inner.put(StateDrawable.class, RuntimeStateDrawable.class);
		inner.put(EAdStateDrawable.class, RuntimeStateDrawable.class);
		inner.put(FramesAnimation.class, RuntimeFramesAnimation.class);
		inner.put(EAdFilteredDrawable.class, RuntimeFilteredDrawable.class);
		inner.put(FilteredDrawable.class, RuntimeFilteredDrawable.class);
		inner.put(Sound.class, GdxSound.class);

		putAll((HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>) inner);
	}

}
