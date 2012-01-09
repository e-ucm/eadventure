package ead.engine.rendering;

import android.graphics.Canvas;

import com.google.inject.Inject;

import ead.common.util.ReflectionProvider;
import ead.engine.core.platform.rendering.AbstractFilterFactory;

public class AndroidFilterFactory extends AbstractFilterFactory<Canvas>{
	
	@Inject
	public AndroidFilterFactory(
			ReflectionProvider interfacesProvider) {
		super(new AndroidFilterProvider(), interfacesProvider);
	}
}
