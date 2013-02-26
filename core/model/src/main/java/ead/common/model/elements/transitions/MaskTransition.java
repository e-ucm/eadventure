package ead.common.model.elements.transitions;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.assets.drawable.EAdDrawable;

@Element
public class MaskTransition extends EmptyTransition {

	@Param
	private EAdDrawable mask;

	public MaskTransition() {

	}

	public MaskTransition(EAdDrawable mask, int time) {
		super(time);
		this.mask = mask;
	}

	public EAdDrawable getMask() {
		return mask;
	}

	public void setMask(EAdDrawable mask) {
		this.mask = mask;
	}

}
