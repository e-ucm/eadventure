package es.eucm.eadventure.engine.core.platform.impl.rendering;

import playn.core.Canvas;
import es.eucm.eadventure.common.resources.assets.drawable.filters.MatrixFilter;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.AbstractMapProvider;
import es.eucm.eadventure.engine.core.platform.impl.rendering.filters.PlayNMatrixFilter;
import es.eucm.eadventure.engine.core.platform.rendering.filters.RuntimeFilter;

public class PlayNFilterProvider extends AbstractMapProvider<Class<?>, RuntimeFilter<?, Canvas>> {

	public PlayNFilterProvider( ){
		factoryMap.put(MatrixFilter.class, new PlayNMatrixFilter());	
	}

}
