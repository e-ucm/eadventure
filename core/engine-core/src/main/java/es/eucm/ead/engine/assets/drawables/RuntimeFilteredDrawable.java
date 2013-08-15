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

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;

import es.eucm.ead.engine.assets.AbstractRuntimeAsset;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.canvas.GdxCanvas;
import es.eucm.ead.engine.canvas.filters.FilterFactory;
import es.eucm.ead.model.assets.drawable.filters.EAdFilteredDrawable;

public class RuntimeFilteredDrawable extends
		AbstractRuntimeAsset<EAdFilteredDrawable> implements
		RuntimeDrawable<EAdFilteredDrawable> {

	private RuntimeDrawable<?> drawable;

	private FilterFactory filterFactory;

	private int time;

	private List<String> states;

	private int level;

	@Inject
	public RuntimeFilteredDrawable(AssetHandler assetHandler,
			FilterFactory filterFactory) {
		super(assetHandler);
		this.filterFactory = filterFactory;
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		drawable = (RuntimeDrawable<?>) assetHandler.getRuntimeAsset(descriptor
				.getDrawable(), true);

		return true;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		if (drawable != null)
			drawable.freeMemory();
		drawable = null;
	}

	@Override
	public int getWidth() {
		// TODO filter could change width and height
		return drawable.getDrawable(time, states, level).getWidth();
	}

	@Override
	public int getHeight() {
		// TODO filter could change width and height
		return drawable.getDrawable(time, states, level).getHeight();
	}

	@Override
	public void render(GdxCanvas c) {
		filterFactory.setFilter(drawable.getDrawable(time, states, level),
				descriptor.getFilter(), c);
		drawable.getDrawable(time, states, level).render(c);
		filterFactory.unsetFilter(drawable.getDrawable(time, states, level),
				descriptor.getFilter(), c);
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO filter could change this
		return drawable.contains(x, y);
	}

	@Override
	public RuntimeDrawable<?> getDrawable(int time, List<String> states,
			int level) {
		this.time = time;
		this.states = states;
		this.level = level;
		return this;
	}

	@Override
	public void refresh() {
		drawable.refresh();
	}

	public Texture getTextureHandle() {
		return getDrawable(time, states, level).getTextureHandle();
	}

}
