package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.model.elements.EAdTransition;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.DisplaceTransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.EmptyTransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.SimpleTransitionGO;
import es.eucm.eadventure.engine.core.platform.TransitionFactory;

public class DesktopTransitionFactory implements TransitionFactory {
	
	private Injector injector;

	@Inject
	public DesktopTransitionFactory(Injector injector) {
		this.injector = injector;
	}
	
	@Override
	public TransitionGO getTransition(EAdTransition transition) {
		switch (transition) {
		case BASIC:
			return injector.getInstance(EmptyTransitionGO.class);
		case DISPLACE:
			return injector.getInstance(DisplaceTransitionGO.class);
		default:
			return injector.getInstance(SimpleTransitionGO.class);
		}
	}

}
