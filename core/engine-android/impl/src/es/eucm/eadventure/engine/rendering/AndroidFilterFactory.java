package es.eucm.eadventure.engine.rendering;

import android.graphics.Canvas;

import com.google.inject.Inject;

import es.eucm.eadventure.common.ReflectionProvider;
import es.eucm.eadventure.engine.core.platform.rendering.AbstractFilterFactory;

public class AndroidFilterFactory extends AbstractFilterFactory<Canvas>{
	
	@Inject
	public AndroidFilterFactory(
			ReflectionProvider interfacesProvider) {
		super(new AndroidFilterProvider(), interfacesProvider);
	}
}
