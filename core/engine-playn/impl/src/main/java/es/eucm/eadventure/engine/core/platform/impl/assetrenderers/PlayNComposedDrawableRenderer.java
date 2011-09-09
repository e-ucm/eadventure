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

package es.eucm.eadventure.engine.core.platform.impl.assetrenderers;

import java.util.logging.Logger;

import playn.core.Surface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;

@Singleton
public class PlayNComposedDrawableRenderer implements AssetRenderer<Surface, RuntimeComposedDrawable> {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger("DesktopComposedDrawableRenderer");

	private AssetHandler assetHandler;
	
	private GraphicRendererFactory<Surface> rendererFactory;
	
	@SuppressWarnings("unchecked")
	@Inject
	public PlayNComposedDrawableRenderer(AssetHandler assetHandler, GraphicRendererFactory<?> rendererFactory) {
		logger.info("New instance");
		this.assetHandler = assetHandler;
		this.rendererFactory = (GraphicRendererFactory<Surface>) rendererFactory;
	}
	
	@Override
	public void render(Surface graphicContext, RuntimeComposedDrawable asset, EAdPosition position, float scale, int offsetX, int offsetY) {
		for (Drawable drawable : asset.getAssetList())
			rendererFactory.render(graphicContext, assetHandler.getRuntimeAsset(drawable), position, scale, offsetX, offsetY);
	}

	@Override
	public boolean contains(int x, int y, RuntimeComposedDrawable asset) {
		boolean contains = false;
		for (Drawable drawable : asset.getAssetList())
			contains = contains || rendererFactory.contains(x, y, assetHandler.getRuntimeAsset(drawable));
		return contains;
	}		

}
