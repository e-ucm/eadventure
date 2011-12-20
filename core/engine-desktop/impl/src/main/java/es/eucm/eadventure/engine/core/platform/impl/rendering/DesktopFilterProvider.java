package es.eucm.eadventure.engine.core.platform.impl.rendering;

import java.awt.Graphics2D;

import es.eucm.eadventure.common.resources.assets.drawable.filters.impl.MatrixFilter;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.AbstractMapProvider;
import es.eucm.eadventure.engine.core.platform.impl.rendering.filters.DesktopMatrixFilter;
import es.eucm.eadventure.engine.core.platform.rendering.filters.RuntimeFilter;

public class DesktopFilterProvider extends AbstractMapProvider<Class<?>, RuntimeFilter<?, Graphics2D>> {

	public DesktopFilterProvider( ){
		factoryMap.put(MatrixFilter.class, new DesktopMatrixFilter());	
	}

}
