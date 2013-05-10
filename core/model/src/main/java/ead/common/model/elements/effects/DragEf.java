package ead.common.model.elements.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;

@Element
public class DragEf extends AbstractEffect {

	@Param
	private boolean returnAfterDrag;

	public boolean isReturnAfterDrag() {
		return returnAfterDrag;
	}

	public void setReturnAfterDrag(boolean returnAfterDrag) {
		this.returnAfterDrag = returnAfterDrag;
	}

}
