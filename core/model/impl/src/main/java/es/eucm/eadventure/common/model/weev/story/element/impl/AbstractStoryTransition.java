package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.impl.AbstractTransition;
import es.eucm.eadventure.common.model.weev.story.elements.Effect;
import es.eucm.eadventure.common.model.weev.story.elements.Node;
import es.eucm.eadventure.common.model.weev.story.elements.StoryList;
import es.eucm.eadventure.common.model.weev.story.elements.StoryTransition;

/**
 * Abstract implementation of {@link StoryTransition}
 *
 * @param <S>
 */
public abstract class AbstractStoryTransition extends AbstractTransition<Node> implements
		StoryTransition {
	
	@Param(value = "effectList")
	StoryList<Effect> effectList;

	@Override
	public StoryList<Effect> getEffectList() {
		return effectList;
	}
	
	public void setEffectList(StoryList<Effect> effectList) {
		this.effectList = effectList;
	}

}
