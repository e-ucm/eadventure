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

package ead.engine.core.platform.assets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.SpriteImage;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.DrawableAsset;

/**
 * Represents a runtime engine sprite image, associated with an
 * {@link AssetDescritpor}
 * 
 */
public abstract class RuntimeSpriteImage<GraphicContext> extends
		AbstractRuntimeAsset<SpriteImage> implements DrawableAsset<SpriteImage, GraphicContext> {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger("RuntimeSpriteImage");

	/**
	 * The asset handler
	 */
	protected AssetHandler assetHandler;

	protected int rows;

	protected int cols;

	@Inject
	public RuntimeSpriteImage(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		logger.info("New instance");
	}

	@Override
	public void setDescriptor(SpriteImage descriptor) {
		this.descriptor = (SpriteImage) descriptor;
		this.rows = (int) Math.sqrt(descriptor.getTotalSprites());
		this.cols = (int) Math.sqrt(descriptor.getTotalSprites());
	}

	@Override
	public void update() {
		assetHandler.getRuntimeAsset(descriptor).update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends EAdDrawable> DrawableAsset<S, GraphicContext> getDrawable() {
		return (DrawableAsset<S, GraphicContext>) this;
	}

	public int getWidth() {
		return assetHandler.getDrawableAsset(descriptor, null).getWidth() / rows;
	}

	public int getHeight() {
		return assetHandler.getDrawableAsset(descriptor, null).getHeight() / cols;
	}

	@Override
	public boolean loadAsset() {
		assetHandler.getRuntimeAsset(descriptor).loadAsset();
		return assetHandler != null;
	}

	@Override
	public void freeMemory() {
		assetHandler.getRuntimeAsset(descriptor).freeMemory();
	}

	@Override
	public boolean isLoaded() {
		return assetHandler.getRuntimeAsset(descriptor).isLoaded();
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}
	
	public int getSprite() {
		return descriptor.getSprite();
	}

	public int getTotalSprites() {
		return descriptor.getTotalSprites();
	}

	protected int getSpriteWidth() {
		int totalWidth = getWidth();
		return totalWidth / cols;
	}
	
	protected int getImageX() {
		return getSpriteWidth() * (getSprite() % cols);
	}
	
	protected int getSpriteHeight() {
		int totalHeight = getHeight();
		return totalHeight / rows;
	}
	
	protected int getImageY() {
		return getSpriteHeight() * (getSprite() / rows);
	}

	public boolean contains(int x, int y) {
		// TODO process image alpha
		return x > 0 && y > 0 && x < getSpriteWidth() && y < getSpriteHeight();
	}

}
