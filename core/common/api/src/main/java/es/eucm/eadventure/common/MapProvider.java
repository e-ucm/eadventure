package es.eucm.eadventure.common;

import java.util.Map;

/**
 * <p>Map provider interface, to provide a map used by
 * factories to find the correspondence between different classes
 * (e.g. assets and their renderers)</p>
 *
 * @param <S> The class of the keys
 * @param <T> The class of the values
 */
public interface MapProvider<S, T> {

	/**
	 * @return the map provided by this class
	 */
	Map<S, T> getMap();
	
}
