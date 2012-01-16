package ead.common.model.elements.transitions;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdElementImpl;

/**
 * Basic empty transition
 *
 */
@Element(detailed = EmptyTransition.class, runtime = EmptyTransition.class)
public class EmptyTransition extends EAdElementImpl implements EAdTransition {
	
	private static final EmptyTransition transition = new EmptyTransition( 0 );
	
	@Param("time")
	private int time;
	
	public EmptyTransition( ){
		this( 0 );
	}
	
	
	public EmptyTransition( int time ){
		this.time = time;
	}

	@Override
	public int getTime() {
		return time;
	}
	
	public void setTime( int time ){
		this.time = time;
	}
	
	public static EmptyTransition instance( ){
		return transition;
	}

}
