package es.eucm.eadventure.common.model.weev.story.section;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.weev.story.StoryElement;
import es.eucm.eadventure.common.params.EAdString;

/**
 * A section or part of a {@link Story} that can be edited independently from
 * the rest of the story
 */
public interface StorySection extends StoryElement {

	/**
	 * @return the name of the section
	 */
	EAdString getName();

	/**
	 * @return the list of {@StorySectionStart}s
	 */
	EAdList<StorySectionStart> getSectionStarts();

}
