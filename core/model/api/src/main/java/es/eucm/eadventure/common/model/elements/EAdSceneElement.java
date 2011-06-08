package es.eucm.eadventure.common.model.elements;

import es.eucm.eadventure.common.interfaces.Positioned;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;

public interface EAdSceneElement extends EAdElement, Positioned {

	/**
	 * Gets the scale of the scene element.
	 * 
	 * @return the scale value.
	 */
	float getScale();

	/**
	 * Returns initial orientation for this actor reference
	 * 
	 * @return initial orientation for this actor reference
	 */
	Orientation getInitialOrientation();

	/**
	 * Returns the runtime variable holding the x position of the scene element
	 * 
	 * @return the runtime variable holding the x position of the scene element
	 */
	EAdVar<Integer> positionXVar();

	/**
	 * Returns the runtime variable holding the y position of the scene element
	 * 
	 * @return the runtime variable holding the y position of the scene element
	 */
	EAdVar<Integer> positionYVar();

	/**
	 * Returns the runtime variable associated to the visibility of the element
	 * 
	 * @return
	 */
	EAdVar<Boolean> visibleVar();

	/**
	 * Returns the runtime variable holding the width of the scene element,
	 * during game
	 * 
	 * @return
	 */
	EAdVar<Integer> widthVar();
	
	/**
	 * Returns the runtime variable holding the height of the scene element,
	 * during game
	 * 
	 * @return
	 */
	EAdVar<Integer> heightVar();

}
