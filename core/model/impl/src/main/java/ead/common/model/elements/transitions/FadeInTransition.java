package ead.common.model.elements.transitions;

import ead.common.interfaces.Element;

@Element(detailed = FadeInTransition.class, runtime = FadeInTransition.class )
public class FadeInTransition extends EmptyTransition {

	public FadeInTransition( int time ){
		super( time );
	}
	
}
