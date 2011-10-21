package es.eucm.eadventure.common.model.weev;

import es.eucm.eadventure.common.model.weev.common.Positioned;

/**
 * A transition from one generic element to another
 *
 * @param <S>
 */
public interface Transition<S> extends Positioned, WEEVElement {

	/**
	 * @return the start element of the transition
	 */
	S getStart();
	
	/**
	 * @return the end element of the transition
	 */
	S getEnd();
	
}
