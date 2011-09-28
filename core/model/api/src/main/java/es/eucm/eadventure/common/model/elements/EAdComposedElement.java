package es.eucm.eadventure.common.model.elements;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.extra.EAdList;

/**
 * Represents an element composed by other elements
 * 
 * 
 * 
 */
public interface EAdComposedElement extends EAdSceneElement {
	
	/**
	 * @return the {@link EAdElement}s that make up the scene
	 */
	EAdList<EAdSceneElement> getElements();

}
