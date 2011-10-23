package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.story.elements.Hint;
import es.eucm.eadventure.common.model.weev.story.elements.Node;
import es.eucm.eadventure.common.model.weev.story.elements.StoryList;
import es.eucm.eadventure.common.model.weev.story.impl.AbstractStoryElement;

/**
 * Abstract implementation of {@link Node}
 */
public abstract class AbstractNode extends AbstractStoryElement implements Node {

	@Param(value = "hintList")
	private StoryList<Hint> hintList;
	
	@Override
	public StoryList<Hint> getHintList() {
		return hintList;
	}
	
	public void setHintList(StoryList<Hint> hintList) {
		this.hintList = hintList;
	}

}
