package es.eucm.eadventure.engine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.graphics.Canvas;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.core.impl.GraphicRendererFactoryImpl;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;

@Singleton
public class AndroidGraphicRendererFactory extends GraphicRendererFactoryImpl<Canvas> {

	@Inject
	public AndroidGraphicRendererFactory(MapProvider<Class<?>, GraphicRenderer<?, ?>> mapProvider) {
		super(mapProvider);
	}

}
