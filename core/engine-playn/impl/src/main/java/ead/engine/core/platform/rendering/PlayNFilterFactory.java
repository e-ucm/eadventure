package ead.engine.core.platform.rendering;

import playn.core.Canvas;

import com.google.inject.Inject;

import ead.common.util.ReflectionProvider;
import ead.engine.core.platform.rendering.AbstractFilterFactory;

public class PlayNFilterFactory extends AbstractFilterFactory<Canvas>{

	@Inject
	public PlayNFilterFactory(ReflectionProvider interfacesProvider) {
		super( new PlayNFilterProvider(), interfacesProvider);
	}

}
