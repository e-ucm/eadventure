package es.eucm.eadventure.common.model.guievents.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;

@Element(detailed = EAdDropEvent.class, runtime = EAdDropEvent.class)
public class EAdDropEvent implements EAdGUIEvent {

	@Param("carryElement")
	private EAdElement carryElement;
	
	public EAdDropEvent(EAdElement object) {
		this.carryElement = object;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdDropEvent) {
			EAdDropEvent e = (EAdDropEvent) o;
			if (e.carryElement == carryElement)
				return true;
			return e.carryElement.equals(carryElement);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Drop " + carryElement.getId();
	}
	

}
