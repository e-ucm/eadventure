package es.eucm.eadventure.common.model.conditions.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.variables.EAdField;

@Element(detailed = FlagCondition.class, runtime = FlagCondition.class)
public class FlagCondition extends AbstractEAdCondition {

	private EAdField<Boolean> flag;

	public FlagCondition(String id) {
		super(id);
	}

	public FlagCondition(EAdField<Boolean> flag) {
		super("flagCondition");
		this.flag = flag;
	}

	public EAdField<Boolean> getFlag() {
		return flag;
	}

}
