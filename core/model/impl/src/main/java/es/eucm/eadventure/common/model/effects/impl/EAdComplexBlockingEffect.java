package es.eucm.eadventure.common.model.effects.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.EAdListImpl;

@Element(detailed = EAdComplexBlockingEffect.class, runtime = EAdComplexBlockingEffect.class)
public class EAdComplexBlockingEffect extends AbstractEAdEffect implements
		EAdEffect {

	protected EAdList<EAdSceneElement> components;

	protected ArrayList<EAdEffect> finalEffects;

	private EAdCondition blockingCondition;

	public EAdComplexBlockingEffect(String id) {
		super(id);
		components = new EAdListImpl<EAdSceneElement>(
				EAdSceneElement.class);
		blockingCondition = EmptyCondition.TRUE_EMPTY_CONDITION;
		finalEffects = new ArrayList<EAdEffect>();
	}

	public EAdList<EAdSceneElement> getComponents() {
		return components;
	}

	public EAdCondition getBlockingCondition() {
		return blockingCondition;
	}

	public void setBlockingCondition(EAdCondition blockingCondition) {
		this.blockingCondition = blockingCondition;
	}

	/**
	 * Return a list with the effects to be executed once this blocking effect
	 * ends
	 * 
	 * @return
	 */
	public List<EAdEffect> getFinalEffects() {
		return finalEffects;
	}
}
