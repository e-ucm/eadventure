package es.eucm.eadventure.engine.core.platform.impl.rendering;

import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.resources.assets.drawable.filters.DrawableFilter;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.rendering.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.rendering.filters.FilterFactory;
import es.eucm.eadventure.engine.core.platform.rendering.filters.RuntimeFilter;

public class AbstractFilterFactory<GraphicContext> extends AbstractFactory<RuntimeFilter<?, GraphicContext>> implements FilterFactory<GraphicContext> {

	public AbstractFilterFactory(
			MapProvider<Class<?>, RuntimeFilter<?, GraphicContext>> mapProvider,
			ReflectionProvider interfacesProvider) {
		super(mapProvider, interfacesProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DrawableFilter> void applyFilter(DrawableAsset<?, GraphicContext> drawable, T filter, EAdCanvas<GraphicContext> c) {
		RuntimeFilter<T, GraphicContext> rf = (RuntimeFilter<T, GraphicContext>) this.get(filter.getClass());
		if ( rf != null )
			rf.applyFilter(drawable, filter, c);
	}

}
