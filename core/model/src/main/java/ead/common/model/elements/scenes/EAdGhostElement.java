package ead.common.model.elements.scenes;

import ead.common.resources.assets.drawable.EAdDrawable;

/**
 * A ghost element is an invisible element which can received events
 * 
 * 
 */
public interface EAdGhostElement extends EAdSceneElement {

	/**
	 * Returns a drawable defining the "contains" area for the element
	 * 
	 * @return
	 */
	EAdDrawable getInteractionArea();

	/**
	 * Sets the defining the "contains" area for the element
	 * 
	 * @return
	 */
	void setInteractionArea(EAdDrawable drawable);

}
