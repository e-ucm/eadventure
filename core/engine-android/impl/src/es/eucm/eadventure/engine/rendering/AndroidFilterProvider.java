package es.eucm.eadventure.engine.rendering;

import android.graphics.Canvas;
import es.eucm.eadventure.common.resources.assets.drawable.filters.MatrixFilter;
import es.eucm.eadventure.engine.core.factorymapproviders.AbstractMapProvider;
import es.eucm.eadventure.engine.core.platform.rendering.filters.RuntimeFilter;
import es.eucm.eadventure.engine.rendering.filters.AndroidMatrixFilter;

public class AndroidFilterProvider extends AbstractMapProvider<Class<?>, RuntimeFilter<?, Canvas>> {

	public AndroidFilterProvider( ){
		factoryMap.put(MatrixFilter.class, new AndroidMatrixFilter());	
	}


}
