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

package ead.common.params.guievents;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.params.AbstractParam;
import ead.common.params.guievents.enums.DragGEvType;

@Element
public class DragGEv extends AbstractParam implements EAdGUIEvent {

	@Param("carryElement")
	private String carryElement;

	@Param("action")
	private DragGEvType action;

	/**
	 * Constructs a drag event from its string representation
	 * 
	 * @param data
	 */
	public DragGEv(String data) {
		parse(data);
	}

	public DragGEv() {

	}

	public DragGEv(String carryElement, DragGEvType action) {
		super();
		this.carryElement = carryElement;
		this.action = action;
	}

	public String getCarryElement() {
		return carryElement;
	}

	public DragGEvType getAction() {
		return action;
	}

	public void setCarryElement(String carryElement) {
		this.carryElement = carryElement;
	}

	public void setAction(DragGEvType action) {
		this.action = action;
	}

	public String toString() {
		return carryElement + ";" + action;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object o) {
		return o instanceof DragGEv && toString().equals(o.toString());
	}

	@Override
	public boolean parse(String data) {
		String[] values = data.split(";");
		if (values.length == 2) {
			try {
				this.carryElement = values[0];
				this.action = DragGEvType.valueOf(values[1]);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public String toStringData() {
		return toString();
	}

}
