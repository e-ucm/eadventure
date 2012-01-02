package ead.engine.rendering;

import android.graphics.Canvas;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.engine.core.factorymapproviders.AbstractMapProvider;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;
import ead.engine.rendering.filters.AndroidMatrixFilter;

public class AndroidFilterProvider extends AbstractMapProvider<Class<?>, RuntimeFilter<?, Canvas>> {

	public AndroidFilterProvider( ){
		factoryMap.put(MatrixFilter.class, new AndroidMatrixFilter());	
	}


}
