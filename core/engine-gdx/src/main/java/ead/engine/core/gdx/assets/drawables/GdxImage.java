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

package ead.engine.core.gdx.assets.drawables;

import com.badlogic.gdx.files.FileHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.inject.Inject;

import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.drawables.basics.RuntimeImage;
import ead.engine.core.platform.rendering.GenericCanvas;

public class GdxImage extends RuntimeImage {

	private static final Logger logger = LoggerFactory.getLogger("GdxImage");

	private FileHandle fh;
	private TextureRegion textureRegion;
	private Pixmap pixmap;

	@Inject
	public GdxImage(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		try {
			fh = ((GdxAssetHandler) assetHandler).getFileHandle(descriptor
					.getUri().getPath());
			pixmap = new Pixmap(fh);
		} catch (Exception e) {
			// TODO Load a default error image.
			logger
					.warn("Cound not load image for descriptor: " + descriptor,
							e);
			pixmap = new Pixmap(64, 64, Pixmap.Format.RGB565);
		}
		Texture texture = new Texture(pixmap);
		textureRegion = new TextureRegion(texture);
		textureRegion.flip(false, true);
		return true;
	}

	@Override
	public boolean contains(int x, int y) {
		if (super.contains(x, y)) {
			int alpha = pixmap.getPixel(x, y) & 255;
			return alpha > 128;
		}
		return false;
	}

	@Override
	public int getWidth() {
		return Math.abs(textureRegion.getRegionWidth());
	}

	@Override
	public int getHeight() {
		return Math.abs(textureRegion.getRegionHeight());
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		textureRegion.getTexture().dispose();
		textureRegion = null;
		pixmap.dispose();
		pixmap = null;
	}

	@Override
	public void render(GenericCanvas c) {
		render((SpriteBatch) c.getNativeGraphicContext());
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, 0, 0);
	}

	@Override
	public void refresh() {
		FileHandle fh = ((GdxAssetHandler) assetHandler)
				.getFileHandle(descriptor.getUri().getPath());
		if (!this.fh.path().equals(fh.path())) {
			this.freeMemory();
			this.loadAsset();
		}
	}
}
