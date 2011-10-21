package es.eucm.eadventure.common.model.weev.story.section;

import es.eucm.eadventure.common.model.weev.adaptation.AdaptationProfile;
import es.eucm.eadventure.common.model.weev.story.elements.Node;

/**
 * A {@link Node} that marks a start for a {@link StorySection}
 */
public interface StorySectionStart extends Node {

	/**
	 * @return the {@link AdaptationProfile} that must be selected for this node
	 *         to be the start of the {@link StorySection}
	 */
	AdaptationProfile getAdaptationProfile();

}
