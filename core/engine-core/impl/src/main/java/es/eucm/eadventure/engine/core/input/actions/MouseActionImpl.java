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

package es.eucm.eadventure.engine.core.input.actions;

import es.eucm.eadventure.common.model.elements.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.elements.guievents.enums.MouseButtonType;
import es.eucm.eadventure.common.model.elements.guievents.enums.MouseEventType;

public class MouseActionImpl extends AbstractInputAction<EAdMouseEvent> {

	/**
	 * Virtual X coordinate where the action was performed.
	 */
	private int virtualX;

	/**
	 * Virtual Y coordinate where the action was performed.
	 */
	private int virtualY;

	public MouseActionImpl(EAdMouseEvent event, int virtualX, int virtualY) {
		super( event );
		this.virtualX = virtualX;
		this.virtualY = virtualY;
	}
	
	public MouseActionImpl(MouseEventType type, MouseButtonType button, int virtualX, int virtualY ){
		this( new EAdMouseEvent( type, button), virtualX, virtualY);
	}

	/**
	 * @return the virtualX
	 */
	public int getVirtualX() {
		return virtualX;
	}

	/**
	 * @return the virtualY
	 */
	public int getVirtualY() {
		return virtualY;
	}

	public MouseEventType getType() {
		return event.getType();
	}

	public MouseButtonType getButton() {
		return event.getButton();
	}

}
