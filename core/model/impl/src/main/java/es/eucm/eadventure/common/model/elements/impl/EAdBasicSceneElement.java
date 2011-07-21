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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.EAdElementVars;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

@Element(detailed = EAdBasicSceneElement.class, runtime = EAdBasicSceneElement.class)
public class EAdBasicSceneElement extends AbstractEAdElementWithBehavior
		implements EAdSceneElement {
	
	@Param("vars")
	private EAdSceneElementVars vars;

	@Param("draggable")
	private EAdCondition draggable;

	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";

	private boolean clone;

	public EAdBasicSceneElement(String id) {
		super(id);
		clone = false;
		draggable = EmptyCondition.FALSE_EMPTY_CONDITION;
		vars = new EAdSceneElementVars( this );
		setPosition(new EAdPosition(0, 0));
		
	}

	public void setPosition(EAdPosition position) {
		vars.getVar(EAdSceneElementVars.VAR_POSITION).setInitialValue(position);
	}


	/**
	 * Sets scale for this reference
	 * 
	 * @param scale
	 *            the scale
	 */
	public void setScale(float scale) {
		vars.getVar(EAdSceneElementVars.VAR_SCALE).setInitialValue(scale);
	}

	/**
	 * Sets the initial orientation for the actor reference
	 * 
	 * @param orientation
	 *            the orientation
	 */
	public void setInitialOrientation(Orientation orientation) {
		vars.getVar(EAdSceneElementVars.VAR_ORIENTATION).setInitialValue(orientation);

	}

	public EAdCondition getDraggabe() {
		return draggable;
	}

	public void setDraggabe(EAdCondition draggable) {
		this.draggable = draggable;
	}

	public String toString() {
		return id + " - Scene element";
	}

	@Override
	public boolean isClone() {
		return clone;
	}

	/**
	 * Sets whether if this scene element must be cloned whenever is added to
	 * the game. This means that all its variables will be set with its initial
	 * values, instead of storing their last values
	 * 
	 * @param clone
	 *            if this elements produces clones when its added to the game
	 */
	public void setClone(boolean clone) {
		this.clone = clone;
	}

	@Override
	public EAdElementVars getVars() {
		return vars;
	}

}
