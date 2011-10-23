package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.story.StoryElement;
import es.eucm.eadventure.common.model.weev.story.elements.Comment;
import es.eucm.eadventure.common.model.weev.story.impl.AbstractStoryElement;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default {@link Comment} implementation
 */
@Element(detailed = CommentImpl.class, runtime = CommentImpl.class)
public class CommentImpl extends AbstractStoryElement implements Comment {

	@Param(value = "storyElement")
	private StoryElement storyElement;
	
	@Param(value = "text")
	private EAdString text;
	
	public CommentImpl() {
		text = EAdString.newEAdString("text");
	}
	
	@Override
	public StoryElement getStoryElement() {
		return storyElement;
	}
	
	public void setStoryElement(StoryElement storyElement) {
		this.storyElement = storyElement;
	}

	@Override
	public EAdString getText() {
		return text;
	}
	
	public void setText(EAdString text) {
		this.text = text;
	}

}
