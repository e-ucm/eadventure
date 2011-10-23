package es.eucm.eadventure.common.model.weev.story.elements;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.weev.common.Retractable;
import es.eucm.eadventure.common.model.weev.story.StoryElement;

/**
 * The list of elements which can be associated to a {@link StoryElement} in the
 * {@link Story} of the {@link WEEVModel}, to be represented visually as a set of elements
 */
public interface StoryList<E> extends StoryElement, Retractable {

	/**
	 * @return the list of elements
	 */
	EAdList<E> getElements();

}
