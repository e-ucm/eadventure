package es.eucm.eadventure.common.model.elements.effects.hud;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.effects.sceneelements.AbstractSceneElementEffect;

/**
 * 
 * Modifies the elements contained by the basic HUD during the game
 * 
 */
public class ModifyHUDEf extends AbstractSceneElementEffect {

	private boolean add;

	/**
	 * Constructs a ModifyHUDEf
	 * @param element
	 *            the element to be added or removed (it can be a field pointing to scene element or a scene element )
	 * @param add
	 *            if true, the element is added to the basic HUD. if false, the
	 *            element is removed from the basic HUD
	 */
	public ModifyHUDEf(EAdElement element, boolean add) {
		this.setSceneElement(element);
		this.add = add;
	}
	
	/**
	 * 
	 * @return if true, the element must be added to the basic HUD. if false, the
	 *            element must be removed from the basic HUD
	 */
	public boolean isAdd( ){
		return add;
	}
}
