package ead.engine.core.gameobjects.factories;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.engine.core.gameobjects.go.transitions.TransitionGO;
import ead.engine.core.gameobjects.transitions.BasicTransitionGO;
import ead.engine.core.gameobjects.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.transitions.FadeInTransitionGO;
import ead.engine.core.platform.GenericInjector;
import ead.engine.core.platform.TransitionFactory;

@Singleton
public class TransitionFactoryImpl implements TransitionFactory {
	
	private GenericInjector injector;
	
	@Inject
	public TransitionFactoryImpl( GenericInjector injector ){
		this.injector = injector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdTransition> TransitionGO<T> getTransition(T transition) {
		TransitionGO<T> transitionGO = null;

		if ( transition instanceof DisplaceTransition )
			transitionGO = (TransitionGO<T>) injector.getInstance(DisplaceTransitionGO.class);
		else if ( transition instanceof FadeInTransition )
			transitionGO = (TransitionGO<T>) injector.getInstance(FadeInTransitionGO.class);
		else if ( transition instanceof EmptyTransition )
			transitionGO = (TransitionGO<T>) injector.getInstance(BasicTransitionGO.class);



		if (transitionGO != null)
			transitionGO.setTransition(transition);
		return transitionGO;
	}

}
