package es.eucm.eadventure.common.params;

import es.eucm.eadventure.common.interfaces.HasURI;

/**
 * General interfaces for eAdventure text fonts
 *
 */
public interface EAdFont extends EAdParam, HasURI {

	public static enum Style {BOLD, PLAIN, ITALIC};
	
	String getName();

	Style getStyle();

	float getSize();

	boolean isTTF();

}
