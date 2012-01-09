package ead.common.model.elements.effects.hud;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.EAdElement;
import ead.common.model.elements.effects.sceneelements.AbstractSceneElementEffect;

/**
 * 
 * Modifies the elements contained by the basic HUD during the game
 * 
 */
@Element(detailed = ModifyHUDEf.class, runtime = ModifyHUDEf.class)
public class ModifyHUDEf extends AbstractSceneElementEffect {

	@Param("add")
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
	
	public ModifyHUDEf( ){
		this( null, true );
	}
	
	/**
	 * 
	 * @return if true, the element must be added to the basic HUD. if false, the
	 *            element must be removed from the basic HUD
	 */
	public boolean getAdd( ){
		return add;
	}
	
	public void setAdd(boolean add){
		this.add = add;
	}
}
