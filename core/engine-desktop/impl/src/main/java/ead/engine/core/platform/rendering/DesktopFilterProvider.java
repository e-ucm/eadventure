package ead.engine.core.platform.rendering;

import java.awt.Graphics2D;

import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.engine.core.factorymapproviders.AbstractMapProvider;
import ead.engine.core.platform.rendering.filters.DesktopMatrixFilter;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;

public class DesktopFilterProvider extends AbstractMapProvider<Class<?>, RuntimeFilter<?, Graphics2D>> {

	public DesktopFilterProvider( ){
		factoryMap.put(MatrixFilter.class, new DesktopMatrixFilter());	
	}

}
