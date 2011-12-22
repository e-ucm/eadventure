package es.eucm.eadventure.engine.core.platform.rendering.filters;

import es.eucm.eadventure.common.resources.assets.drawable.filters.DrawableFilter;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.rendering.GenericCanvas;

public interface FilterFactory<GraphicContext> {

	<T extends DrawableFilter> void applyFilter(DrawableAsset<?, GraphicContext> drawable, T filter, GenericCanvas<GraphicContext> c );

}
