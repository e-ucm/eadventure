package ead.engine.core.platform.rendering;

import playn.core.Canvas;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.engine.core.factorymapproviders.AbstractMapProvider;
import ead.engine.core.platform.rendering.filters.PlayNMatrixFilter;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;

public class PlayNFilterProvider extends AbstractMapProvider<Class<?>, RuntimeFilter<?, Canvas>> {

	public PlayNFilterProvider( ){
		factoryMap.put(MatrixFilter.class, new PlayNMatrixFilter());	
	}

}
