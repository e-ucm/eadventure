package es.eucm.eadventure.engine.core.platform.impl.extra;

import com.google.inject.Provider;
import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGraphicRendererFactory;

public class GraphicRendererFactoryProvider implements Provider<GraphicRendererFactory<?>> {

	private PlayNGraphicRendererFactory graphicRendererFactory;

	private final ReflectionProvider interfaceProvider;
	
	private final AssetRendererMapProviderFactory assetRendererMapProviderFactory;
	
	@Inject
	public GraphicRendererFactoryProvider(AssetRendererMapProviderFactory assetRendererMapProviderFactory,
			ReflectionProvider interfaceProvider) {
		this.assetRendererMapProviderFactory = assetRendererMapProviderFactory;
		this.interfaceProvider = interfaceProvider;
	}
	
	@Override
	public GraphicRendererFactory<?> get() {
		if (graphicRendererFactory == null) {
			graphicRendererFactory = new PlayNGraphicRendererFactory(interfaceProvider);
			graphicRendererFactory.setMapProvider(assetRendererMapProviderFactory.create(graphicRendererFactory));
		}
		return graphicRendererFactory;
	}

}
