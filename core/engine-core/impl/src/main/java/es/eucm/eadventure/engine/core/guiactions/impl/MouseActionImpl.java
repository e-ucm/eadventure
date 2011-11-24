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

package es.eucm.eadventure.engine.core.guiactions.impl;

import es.eucm.eadventure.common.model.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.guievents.enums.MouseActionType;
import es.eucm.eadventure.common.model.guievents.enums.MouseButton;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;

public class MouseActionImpl implements MouseAction {

	/**
	 * Virtual X coordinate where the action was performed.
	 */
	private int virtualX;

	/**
	 * Virtual Y coordinate where the action was performed.
	 */
	private int virtualY;

	private EAdMouseEvent mouseEvent;

	private boolean consummed;

	public MouseActionImpl(EAdMouseEvent event, int virtualX, int virtualY) {
		mouseEvent = event;
		this.virtualX = virtualX;
		this.virtualY = virtualY;
	}
	
	public MouseActionImpl(MouseActionType type, MouseButton button, int virtualX, int virtualY ){
		this( new EAdMouseEventImpl( type, button), virtualX, virtualY);
	}

	/**
	 * @return the virtualX
	 */
	@Override
	public int getVirtualX() {
		return virtualX;
	}

	/**
	 * @return the virtualY
	 */
	@Override
	public int getVirtualY() {
		return virtualY;
	}

	/**
	 * @return the actionType
	 */
	@Override
	public MouseActionType getType() {
		return mouseEvent.getType();
	}

	@Override
	public boolean isConsumed() {
		return consummed;
	}

	@Override
	public void consume() {
		this.consummed = true;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MouseAction) {
			MouseAction a = (MouseAction) o;
			return a.getType().equals(this.getType());
		}
		return false;
	}

	@Override
	public EAdGUIEvent getGUIEvent() {
		return mouseEvent;
	}

	@Override
	public String toString() {
		return mouseEvent.toString() + " in (" + virtualX + ", " + virtualY
				+ ")";
	}

}
