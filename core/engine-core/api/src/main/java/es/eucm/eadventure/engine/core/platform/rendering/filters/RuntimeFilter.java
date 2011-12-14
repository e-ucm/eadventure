package es.eucm.eadventure.engine.core.platform.rendering.filters;

import es.eucm.eadventure.common.resources.assets.drawable.filters.DrawableFilter;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.rendering.EAdCanvas;

/**
 * A runtime filter 
 * 
 */
public interface RuntimeFilter<T extends DrawableFilter, GraphicContext> {
	
	void applyFilter( DrawableAsset<?, GraphicContext> drawable, T filter, EAdCanvas<GraphicContext> c );

}
