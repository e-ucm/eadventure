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

package es.eucm.eadventure.common.model.guievents.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;

@Element(detailed = EAdDropEvent.class, runtime = EAdDropEvent.class)
public class EAdDropEvent extends EAdElementImpl implements EAdGUIEvent {

	@Param("carryElement")
	private EAdElement carryElement;
	
	public EAdDropEvent(){
		
	}

	public EAdDropEvent(EAdElement object) {
		super( "dropEvent_" + object.getId());
		this.carryElement = object;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EAdDropEvent) {
			EAdDropEvent e = (EAdDropEvent) o;
			if (e.carryElement == carryElement)
				return true;
			return e.carryElement.equals(carryElement);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Drop " + carryElement.getId();
	}

	@Override
	public EAdElement copy() {
		return new EAdDropEvent(carryElement);

	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		if (deepCopy) {
			EAdElement element = carryElement.copy(true);
			return new EAdDropEvent(element);
		} else
			return copy();
	}

}
