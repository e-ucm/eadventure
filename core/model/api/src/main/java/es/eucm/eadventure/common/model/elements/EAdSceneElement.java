package es.eucm.eadventure.common.model.elements;

import es.eucm.eadventure.common.interfaces.Positioned;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;
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
	
	/**
	 * Returns the effects list associated with the given GUI event,
	 * {@code null} if there is no effects associated. This method shouldn't be
	 * used to add new effects to the actor. Returned list could be {@code null}
	 * 
	 * @param event
	 *            the GUI event
	 * @return the effects list associated with the given event
	 */
	EAdElementList<EAdEffect> getEffects(EAdGUIEvent event);

}
