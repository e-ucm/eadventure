package es.eucm.eadventure.engine;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.model.elements.EAdTransition;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.SimpleTransitionGO;
import es.eucm.eadventure.engine.core.platform.TransitionFactory;

public class AndroidTransitionFactory implements TransitionFactory {
	
	private Injector injector;

	@Inject
	public AndroidTransitionFactory(Injector injector) {
		this.injector = injector;
	}
	
	@Override
	public TransitionGO getTransition(EAdTransition transition) {
		switch (transition) {
		//TODO no android transitions?
		case BASIC:
		case DISPLACE:
		default:
			return injector.getInstance(SimpleTransitionGO.class);
		}
	}

}
