package es.eucm.eadventure.common.model.elements;

import java.util.List;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.interfaces.Positioned;
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
	EAdVar<Float> scaleVar();

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
	 * Returns the runtime variable holding the rotation of the scene element
	 * 
	 * @return
	 */
	EAdVar<Float> rotationVar();

	/**
	 * Returns the runtime variable holding the alpha for the scene element
	 * 
	 * @return
	 */
	EAdVar<Float> alphaVar();

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

	/**
	 * Returns true if this scene element must be cloned whenever is added to
	 * the game. This means that all its variables will be set with its initial
	 * values, instead of storing their last values
	 * 
	 * @return if this element must be cloned whenever is added to the game
	 */
	boolean isClone();

	/**
	 * Returns a list of all vars of this scene element
	 * 
	 * @return a list of all vars of this scene element
	 */
	List<EAdVar<?>> getVars();

}
