package es.eucm.eadventure.common.model.weev.story.section.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.story.impl.AbstractStoryElement;
import es.eucm.eadventure.common.model.weev.story.section.StorySection;
import es.eucm.eadventure.common.model.weev.story.section.StorySectionStart;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default implementation of {@link StorySection}
 */
@Element(detailed = StorySectionImpl.class, runtime = StorySectionImpl.class)
public class StorySectionImpl extends AbstractStoryElement implements
		StorySection {
	
	@Param(value = "name")
	private EAdString name;
	
	private EAdList<StorySectionStart> sectionStarts;
	
	public StorySectionImpl() {
		name = EAdString.newEAdString("name");
		sectionStarts = new EAdListImpl<StorySectionStart>(StorySectionStart.class);
	}

	@Override
	public EAdString getName() {
		return name;
	}

	@Override
	public EAdList<StorySectionStart> getSectionStarts() {
		return sectionStarts;
	}

}
