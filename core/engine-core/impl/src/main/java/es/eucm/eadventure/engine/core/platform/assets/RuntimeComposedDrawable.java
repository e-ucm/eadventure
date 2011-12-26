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

package es.eucm.eadventure.engine.core.platform.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.extra.EAdList;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.DisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.rendering.GenericCanvas;

/**
 * Represents a runtime engine composed drawable, associated with an
 * {@link AssetDescritpor}
 * 
 */
public class RuntimeComposedDrawable<GraphicContext> extends
		AbstractRuntimeAsset<ComposedDrawable> implements
		DrawableAsset<ComposedDrawable, GraphicContext> {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger("RuntimeComposedDrawable");

	/**
	 * The asset handler
	 */
	protected AssetHandler assetHandler;

	protected ArrayList<DrawableAsset<?, GraphicContext>> drawables;
	
	@Inject
	public RuntimeComposedDrawable(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		this.drawables = new ArrayList<DrawableAsset<?, GraphicContext>>();
		logger.info("New instance");
	}

	@SuppressWarnings("unchecked")
	public void setDescriptor(ComposedDrawable e) {
		super.setDescriptor(e);
		drawables.clear();
		for (DisplacedDrawable d : e.getAssetList()) {
			drawables.add((DrawableAsset<?, GraphicContext>) assetHandler.getDrawableAsset(d, null));
		}
	}

	@Override
	public void update() {
		for (DrawableAsset<?, GraphicContext> d : getAssets())
			d.update();
	}

	public List<DrawableAsset<?, GraphicContext>> getAssets() {
		return drawables;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S, GraphicContext> getDrawable() {
		return (DrawableAsset<S, GraphicContext>) this;
	}

	public int getWidth() {
		int width = 0;
		for (Drawable asset : descriptor.getAssetList())
			width = Math.max(assetHandler.getDrawableAsset(asset, null).getWidth(), width);
		return width;
	}

	public int getHeight() {
		int height = 0;
		for (Drawable asset : descriptor.getAssetList())
			height = Math.max(assetHandler.getDrawableAsset(asset, null).getHeight(), height);
		return height;
	}

	@Override
	public boolean loadAsset() {
		for (Drawable asset : descriptor.getAssetList())
			assetHandler.getRuntimeAsset(asset).loadAsset();
		return assetHandler != null;
	}

	@Override
	public void freeMemory() {
		for (Drawable asset : descriptor.getAssetList())
			assetHandler.getRuntimeAsset(asset).freeMemory();
	}

	@Override
	public boolean isLoaded() {
		boolean loaded = true;
		for (Drawable asset : descriptor.getAssetList())
			loaded = loaded & assetHandler.getRuntimeAsset(asset).isLoaded();
		return loaded;
	}

	public void render(GenericCanvas<GraphicContext> c) {
		for (DrawableAsset<?, GraphicContext> d : getAssets()) {
			d.render(c);
		}
	}

	@Override
	public boolean contains(int x, int y) {
		for (DrawableAsset<?, GraphicContext> d : getAssets()) {
			if (d.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	public EAdList<DisplacedDrawable> getAssetList() {
		return descriptor.getAssetList();
	}

}
