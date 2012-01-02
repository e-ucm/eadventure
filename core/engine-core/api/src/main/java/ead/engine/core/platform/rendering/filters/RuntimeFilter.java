package ead.engine.core.platform.rendering.filters;

import ead.common.resources.assets.drawable.filters.DrawableFilter;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.rendering.GenericCanvas;

/**
 * A runtime filter 
 * 
 */
public interface RuntimeFilter<T extends DrawableFilter, GraphicContext> {
	
	void applyFilter( DrawableAsset<?, GraphicContext> drawable, T filter, GenericCanvas<GraphicContext> c );

}
