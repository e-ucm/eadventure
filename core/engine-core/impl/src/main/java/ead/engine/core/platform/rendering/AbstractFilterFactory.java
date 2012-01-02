package ead.engine.core.platform.rendering;

import ead.common.AbstractFactory;
import ead.common.MapProvider;
import ead.common.ReflectionProvider;
import ead.common.resources.assets.drawable.filters.DrawableFilter;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.platform.rendering.filters.FilterFactory;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;

public class AbstractFilterFactory<GraphicContext> extends AbstractFactory<RuntimeFilter<?, GraphicContext>> implements FilterFactory<GraphicContext> {

	public AbstractFilterFactory(
			MapProvider<Class<?>, RuntimeFilter<?, GraphicContext>> mapProvider,
			ReflectionProvider interfacesProvider) {
		super(mapProvider, interfacesProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DrawableFilter> void applyFilter(DrawableAsset<?, GraphicContext> drawable, T filter, GenericCanvas<GraphicContext> c) {
		RuntimeFilter<T, GraphicContext> rf = (RuntimeFilter<T, GraphicContext>) this.get(filter.getClass());
		if ( rf != null )
			rf.applyFilter(drawable, filter, c);
	}

}
