package es.eucm.eadventure.common.params;

import es.eucm.eadventure.common.interfaces.HasURI;

/**
 * General interfaces for eAdventure text fonts
 * 
 */
public interface EAdFont extends EAdParam, HasURI {

	public static enum Style {
		BOLD, PLAIN, ITALIC
	};

	/**
	 * Returns the font name
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Returns the font style
	 * 
	 * @return
	 */
	Style getStyle();

	/**
	 * @return the size
	 */
	float getSize();

	/**
	 * Returns whether the font is a True Type Font
	 * 
	 * @return
	 */
	boolean isTTF();

}
