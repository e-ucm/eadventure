package ead.engine.core.gdx.platform;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.gdx.platform.filters.FilterMapProvider;
import ead.engine.core.platform.rendering.AbstractFilterFactory;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class GdxFilterFactory extends AbstractFilterFactory<SpriteBatch> {

	@Inject
	public GdxFilterFactory(ReflectionProvider interfacesProvider) {
		super(new FilterMapProvider(), interfacesProvider);
	}

}
