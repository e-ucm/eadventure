package ead.common.model.elements.transitions;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;

@Element(detailed = DisplaceTransition.class, runtime = DisplaceTransition.class)
public class DisplaceTransition extends EmptyTransition {
	
	@Param("type")
	private DisplaceTransitionType type;
	
	@Param("forward")
	private boolean forward;
	
	public DisplaceTransition( ){
		
	}
	
	public DisplaceTransition(int time, DisplaceTransitionType type, boolean forward ) {
		super(time);
		this.type = type;
		this.forward = forward;
	}

	public DisplaceTransitionType getType() {
		return type;
	}

	public void setType(DisplaceTransitionType type) {
		this.type = type;
	}

	public boolean getForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

}
