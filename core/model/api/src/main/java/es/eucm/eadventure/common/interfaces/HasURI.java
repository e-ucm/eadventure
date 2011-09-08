package es.eucm.eadventure.common.interfaces;

import es.eucm.eadventure.common.params.EAdURI;

/**
 * Implemented by those elements which has an URI
 * 
 */
public interface HasURI {

	/**
	 * Returns the URI associated to this element
	 * 
	 * @return
	 */
	EAdURI getURI();
}
