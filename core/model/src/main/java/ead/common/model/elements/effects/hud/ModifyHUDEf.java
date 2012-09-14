/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

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
@Element
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
