package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.impl.AbstractWEEVElement;
import es.eucm.eadventure.common.model.weev.story.elements.Hint;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default {@link Hint} implementation
 */
@Element(detailed = HintImpl.class, runtime = HintImpl.class)
public class HintImpl extends AbstractWEEVElement implements Hint {

	@Param(value = "hint")
	private EAdString hint;
	
	@Param(value = "value")
	private Integer value;
	
	@Override
	public EAdString getHint() {
		return hint;
	}
	
	public void setHint(EAdString hint) {
		this.hint = hint;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}

}
