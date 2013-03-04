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

package ead.engine.core.assets.drawables;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.google.inject.Inject;

import ead.common.model.assets.drawable.basics.NinePatchImage;
import ead.engine.core.assets.AbstractRuntimeAsset;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.AssetHandlerImpl;
import ead.engine.core.canvas.GdxCanvas;

public class RuntimeNinePatchImage extends AbstractRuntimeAsset<NinePatchImage>
		implements RuntimeDrawable<NinePatchImage> {

	private FileHandle fh;
	private NinePatch ninePatch;
	private int width;
	private int height;

	@Inject
	public RuntimeNinePatchImage(AssetHandler assetHandler) {
		super(assetHandler);
	}

	public boolean loadAsset() {
		super.loadAsset();
		fh = ((AssetHandlerImpl) assetHandler).getFileHandle(descriptor
				.getUri());
		Texture t = new Texture(fh);
		this.width = t.getWidth();
		this.height = t.getHeight();
		ninePatch = new NinePatch(t, descriptor.getLeft(), descriptor
				.getRight(), descriptor.getTop(), descriptor.getBottom());
		return true;
	}

	@Override
	public void refresh() {
		FileHandle fh = ((AssetHandlerImpl) assetHandler)
				.getFileHandle(descriptor.getUri());
		if (!this.fh.path().equals(fh.path())) {
			this.freeMemory();
			this.loadAsset();
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void render(GdxCanvas c) {
		ninePatch.draw(c, 0, 0, width, height);
	}

	@Override
	public boolean contains(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	@Override
	public RuntimeDrawable<?> getDrawable(int time, List<String> states,
			int level) {
		return this;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHieght(int height) {
		this.height = height;
	}

	@Override
	public Texture getTextureHandle() {
		if (ninePatch != null) {
			return ninePatch.getTexture();
		}
		return null;
	}

}
