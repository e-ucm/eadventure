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

package ead.engine.core.canvas.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.ead.model.assets.drawable.filters.EAdDrawableFilter;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.drawables.RuntimeDrawable;
import ead.engine.core.canvas.GdxCanvas;
import es.eucm.ead.tools.AbstractFactory;
import es.eucm.ead.tools.reflection.ReflectionProvider;

@Singleton
public class GdxFilterFactory extends AbstractFactory<RuntimeFilter<?>>
		implements FilterFactory {

	@Inject
	public GdxFilterFactory(ReflectionProvider interfacesProvider,
			AssetHandler assetHandler) {
		super(new FilterMapProvider(assetHandler), interfacesProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdDrawableFilter> void setFilter(
			RuntimeDrawable<?> drawable, T filter, GdxCanvas c) {
		RuntimeFilter<T> rf = (RuntimeFilter<T>) this.get(filter.getClass());
		if (rf != null)
			rf.setFilter(drawable, filter, c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdDrawableFilter> void unsetFilter(
			RuntimeDrawable<?> drawable, T filter, GdxCanvas c) {
		RuntimeFilter<T> rf = (RuntimeFilter<T>) this.get(filter.getClass());
		if (rf != null)
			rf.unsetFilter(drawable, filter, c);

	}

}
