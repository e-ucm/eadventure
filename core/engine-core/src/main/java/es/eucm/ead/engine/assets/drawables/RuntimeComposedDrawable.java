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

package es.eucm.ead.engine.assets.drawables;

import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AbstractRuntimeAsset;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.canvas.GdxCanvas;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.EAdBasicDrawable;
import es.eucm.ead.model.assets.drawable.compounds.EAdComposedDrawable;
import es.eucm.ead.model.params.util.Position;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a runtime engine composed drawable
 */
public class RuntimeComposedDrawable extends
		AbstractRuntimeAsset<EAdComposedDrawable> implements
		RuntimeDrawable<EAdComposedDrawable> {

	/**
	 * Logger
	 */
	static private Logger logger = LoggerFactory
			.getLogger(RuntimeComposedDrawable.class);

	protected ArrayList<RuntimeDrawable<?>> drawables;

	private int time;

	private List<String> states;

	private int level;

	@Inject
	public RuntimeComposedDrawable(AssetHandler assetHandler) {
		super(assetHandler);
		this.drawables = new ArrayList<RuntimeDrawable<?>>();
	}

	public void setDescriptor(EAdComposedDrawable e) {
		super.setDescriptor(e);
		drawables.clear();
		for (EAdBasicDrawable d : e.getAssetList()) {
			drawables.add(assetHandler.getDrawableAsset(d));
		}
	}

	public List<RuntimeDrawable<?>> getAssets() {
		return drawables;
	}

	public int getWidth() {
		int width = 0;
		for (EAdDrawable asset : descriptor.getAssetList())
			width = Math.max(assetHandler.getDrawableAsset(asset).getWidth(),
					width);
		return width;
	}

	public int getHeight() {
		int height = 0;
		for (EAdDrawable asset : descriptor.getAssetList())
			height = Math.max(assetHandler.getDrawableAsset(asset).getHeight(),
					height);
		return height;
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		for (RuntimeDrawable<?> d : this.drawables) {
			if (!d.isLoaded())
				d.loadAsset();
		}
		return true;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		for (EAdDrawable asset : descriptor.getAssetList())
			assetHandler.getRuntimeAsset(asset).freeMemory();
	}

	public void render(GdxCanvas c) {
		int i = 0;
		for (RuntimeDrawable<?> d : getAssets()) {
			Position p = descriptor.getPositions().get(i);
			int dispX = p.getJavaX(d.getWidth());
			int dispY = p.getJavaY(d.getHeight());
			c.save();
			c.translate(dispX, dispY);
			d.render(c);
			c.restore();
			i++;
		}
	}

	@Override
	public boolean contains(int x, int y) {
		int i = 0;
		for (RuntimeDrawable<?> d : getAssets()) {
			Position p = descriptor.getPositions().get(i);
			int dispX = p.getJavaX(d.getWidth());
			int dispY = p.getJavaY(d.getHeight());
			if (d.contains(x - dispX, y - dispY)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RuntimeDrawable<?> getDrawable(int time, List<String> states,
			int level) {
		this.time = time;
		this.states = states;
		this.level = level;
		for (RuntimeDrawable<?> d : this.drawables) {
			d.getDrawable(time, states, level);
		}
		return this;
	}

	@Override
	public void refresh() {
		for (RuntimeDrawable<?> d : this.drawables) {
			d.refresh();
		}
	}

	public Texture getTextureHandle() {
		return getDrawable(time, states, level).getTextureHandle();
	}

}
