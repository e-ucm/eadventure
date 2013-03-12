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

package ead.common.model.elements.effects.timedevents;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.effects.AbstractEffect;

/**
 * 
 * Effect that produces a delay in the game. It waits the specified time until
 * disappear
 * 
 */
@Element
public class WaitEf extends AbstractEffect {

	/**
	 * Time to wait for this effect, in milliseconds
	 */
	@Param
	private int time;

	@Param
	private boolean waitUntilClick;

	/**
	 * Constructs a blocking and opaque wait effect with time = 0
	 * 
	 * @param parent
	 *            Element's parent
	 */
	public WaitEf() {
		this(0);
	}

	/**
	 * Constructs a blocking and opaque wait effect with the given time
	 * 
	 * @param parent
	 *            Element's parent
	 * @param time
	 *            the time for this effect
	 */
	public WaitEf(int time) {
		super();
		this.time = time;
	}

	/**
	 * Sets the time to wait for this effect
	 * 
	 * @param time
	 *            the time (in milliseconds)
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * 
	 * @return the time to wait for this effect
	 */
	public int getTime() {
		return time;
	}

	public void setWaitUntilClick(boolean b) {
		this.waitUntilClick = b;
	}

	public boolean isWaitUntilClick() {
		return waitUntilClick;
	}

}
