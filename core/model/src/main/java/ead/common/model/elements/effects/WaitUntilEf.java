package ead.common.model.elements.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdCondition;

@Element
public class WaitUntilEf extends AbstractEffect {

	@Param
	private EAdCondition waitCondition;

	public WaitUntilEf() {

	}

	public WaitUntilEf(EAdCondition waitCondition) {
		super();
		this.waitCondition = waitCondition;
	}

	public EAdCondition getWaitCondition() {
		return waitCondition;
	}

	public void setWaitCondition(EAdCondition waitCondition) {
		this.waitCondition = waitCondition;
	}

}
