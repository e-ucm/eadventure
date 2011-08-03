package es.eucm.eadventure.common.interfaces;

import es.eucm.eadventure.common.resources.EAdURI;

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
