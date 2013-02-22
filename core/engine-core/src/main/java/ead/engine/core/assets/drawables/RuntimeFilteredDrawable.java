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

import com.google.inject.Inject;

import ead.common.model.assets.drawable.filters.EAdFilteredDrawable;
import ead.engine.core.assets.AbstractRuntimeAsset;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.canvas.GdxCanvas;
import ead.engine.core.canvas.filters.FilterFactory;

public class RuntimeFilteredDrawable extends
		AbstractRuntimeAsset<EAdFilteredDrawable> implements
		RuntimeDrawable<EAdFilteredDrawable> {

	private RuntimeDrawable<?> drawable;

	private FilterFactory filterFactory;

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
		return drawable.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO filter could change width and height
		return drawable.getHeight();
	}

	@Override
	public void render(GdxCanvas c) {
		filterFactory.setFilter(drawable, descriptor.getFilter(), c);
		drawable.render(c);
		filterFactory.unsetFilter(drawable, descriptor.getFilter(), c);
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO filter could change this
		return drawable.contains(x, y);
	}

	@Override
	public RuntimeDrawable<?> getDrawable(int time, List<String> states,
			int level) {
		return this;
	}

	@Override
	public void refresh() {
		drawable.refresh();
	}

}
