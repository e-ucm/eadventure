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

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdDropEvent;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.DropAction;

public class DropActionImpl implements DropAction {

	/**
	 * Virtual X coordinate where the action was performed.
	 */
	private int virtualX;

	/**
	 * Virtual Y coordinate where the action was performed.
	 */
	private int virtualY;
	
	private boolean consumed;
	
	private GameObject<? extends EAdElement> draggingElement;

	public DropActionImpl(int virtualX, int virtualY, GameObject<? extends EAdElement> draggingElement) {
		this.virtualX = virtualX;
		this.virtualY = virtualY;
		this.draggingElement = draggingElement;
		consumed = false;
	}

	
	@Override
	public EAdGUIEvent getGUIEvent() {
		return new EAdDropEvent(draggingElement.getElement());
	}

	@Override
	public boolean isConsumed() {
		return consumed;
	}

	@Override
	public void consume() {
		consumed = true;
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


	@Override
	public MouseActionType getType() {
		return MouseActionType.DROP;
	}

}
