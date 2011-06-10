package es.eucm.eadventure.common.resources.assets.multimedia.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;

/**
 * Basic sound implementation
 * 
 */
public class SoundImpl implements Sound {

	@Param("uri")
	private String uri;

	/**
	 * Creates a sound asset with the given URI
	 * 
	 * @param uri
	 *            the URI
	 */
	public SoundImpl(String uri) {
		this.uri = uri;
	}

	public String getURI() {
		return uri;
	}

}
