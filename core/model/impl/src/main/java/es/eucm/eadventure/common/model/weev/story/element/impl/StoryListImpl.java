package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.story.elements.StoryList;
import es.eucm.eadventure.common.model.weev.story.impl.AbstractRetractableStoryElement;

public class StoryListImpl<E> extends AbstractRetractableStoryElement implements StoryList<E> {

	EAdList<E> hints;
	
	public StoryListImpl(Class<E> clazz) {
		hints = new EAdListImpl<E>(clazz);
	}
	
	@Override
	public EAdList<E> getElements() {
		return hints;
	}

}
