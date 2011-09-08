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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;

@Element(detailed = EAdMoveActiveElement.class, runtime = EAdMoveActiveElement.class)
public class EAdMoveActiveElement extends AbstractEAdEffect {

	public static final int MOUSE_COORDINATE = -1;

	@Param("targetX")
	private int targetX;

	@Param("targetY")
	private int targetY;

	public EAdMoveActiveElement(String id) {
		super(id);
		targetX = MOUSE_COORDINATE;
		targetY = MOUSE_COORDINATE;
	}

	/**
	 * Return the x coordinate to move the active element. If x is
	 * {@link EAdMoveActiveElement#MOUSE_COORDINATE}, the target will be the
	 * mouse position
	 * 
	 * @return
	 */
	public int getTargetX() {
		return targetX;
	}

	/**
	 * Sets the x coordinate to move the active element. If x is
	 * {@link EAdMoveActiveElement#MOUSE_COORDINATE}, the target will be the
	 * mouse position
	 * 
	 * @return
	 */
	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	/**
	 * Return the y coordinate to move the active element. If y is
	 * {@link EAdMoveActiveElement#MOUSE_COORDINATE}, the target will be the
	 * mouse position
	 */
	public int getTargetY() {
		return targetY;
	}

	/**
	 * Sets the y coordinate to move the active element. If x is
	 * {@link EAdMoveActiveElement#MOUSE_COORDINATE}, the target will be the
	 * mouse position
	 * 
	 * @return
	 */
	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

}
