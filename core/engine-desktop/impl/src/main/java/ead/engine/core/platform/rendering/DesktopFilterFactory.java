package ead.engine.core.platform.rendering;

import java.awt.Graphics2D;

import com.google.inject.Inject;

import ead.common.util.ReflectionProvider;
import ead.engine.core.platform.rendering.AbstractFilterFactory;

public class DesktopFilterFactory extends AbstractFilterFactory<Graphics2D>{

	@Inject
	public DesktopFilterFactory(ReflectionProvider interfacesProvider) {
		super( new DesktopFilterProvider(), interfacesProvider);
	}

}
