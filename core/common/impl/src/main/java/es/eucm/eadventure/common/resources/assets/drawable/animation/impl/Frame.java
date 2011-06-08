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

package es.eucm.eadventure.common.resources.assets.drawable.animation.impl;

import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class Frame extends ImageImpl implements Image {

	/**
	 * Default frame time in milliseconds
	 */
	public static final int DEFAULT_FRAME_TIME = 300;

	/**
	 * Frame time in milliseconds
	 */
	@Param("time")
	private int time;

	public Frame(String uri) {
		super(uri);
		this.time = DEFAULT_FRAME_TIME;
	}

	public Frame(String uri, int time) {
		super(uri);
		this.time = time;
	}

	/**
	 * Constructor for {@link Frame}
	 * 
	 * @param time
	 *            Time for this frame, in milliseconds
	 */
	public Frame(int time) {
		this.time = time;
	}

	/**
	 * Constructor for {@link Frame} . Sets frame time to
	 * <i>DEFAULT_FRAME_TIME</i>.
	 * 
	 */
	public Frame() {
		this(DEFAULT_FRAME_TIME);
	}

	/**
	 * Sets the time for this frame (in milliseconds)
	 * 
	 * @param time
	 *            the time for this frame (in milliseconds)
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Returns the time for this frame
	 * 
	 * @return the time for this frame
	 */
	public int getTime() {
		return time;
	}

}
