package es.eucm.eadventure.common.model.effects;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;

/**
 * 
 * Implemented by those {@link EAdEffect} that need an {@link EAdSceneElement}
 * 
 */
public interface EAdSceneElementEffect extends EAdEffect {
	
	/**
	 * Returns the scene element associated to this effect
	 * @return
	 */
	EAdSceneElement getSceneElement();
	
	/**
	 * Sets the scene element
	 * @param sceneElement the scene element
	 */
	void setSceneElement( EAdSceneElement sceneElement );

}
