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

package ead.engine.core.platform.rendering;

import ead.common.resources.assets.drawable.filters.EAdDrawableFilter;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.platform.rendering.filters.FilterFactory;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;
import ead.tools.AbstractFactory;
import ead.tools.MapProvider;
import ead.tools.reflection.ReflectionProvider;

public class AbstractFilterFactory<GraphicContext> extends
		AbstractFactory<RuntimeFilter<?, GraphicContext>> implements
		FilterFactory<GraphicContext> {

	public AbstractFilterFactory(
			MapProvider<Class<?>, RuntimeFilter<?, GraphicContext>> mapProvider,
			ReflectionProvider interfacesProvider) {
		super(mapProvider, interfacesProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdDrawableFilter> void applyFilter(
			RuntimeDrawable<?, GraphicContext> drawable, T filter,
			GenericCanvas<GraphicContext> c) {
		RuntimeFilter<T, GraphicContext> rf = (RuntimeFilter<T, GraphicContext>) this
				.get(filter.getClass());
		if (rf != null)
			rf.applyFilter(drawable, filter, c);
	}

}
