package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;

@Element(detailed = EAdComplexBlockingEffect.class, runtime = EAdComplexBlockingEffect.class)
public class EAdComplexBlockingEffect extends AbstractEAdEffect implements
		EAdEffect {
	
	protected EAdElementList<EAdSceneElement> components;

	private EAdCondition blockingCondition;
	
	public EAdComplexBlockingEffect(String id) {
		super(id);
		components = new EAdElementListImpl<EAdSceneElement>();
		blockingCondition = EmptyCondition.TRUE_EMPTY_CONDITION;
	}
	
	public EAdElementList<EAdSceneElement> getComponents() {
		return components;
	}

	public EAdCondition getBlockingCondition() {
		return blockingCondition;
	}
	
	public void setBlockingCondition(EAdCondition blockingCondition) {
		this.blockingCondition = blockingCondition;
	}
}
