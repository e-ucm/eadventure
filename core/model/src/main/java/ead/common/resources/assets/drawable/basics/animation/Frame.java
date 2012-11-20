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

package ead.common.resources.assets.drawable.basics.animation;

import ead.common.interfaces.Param;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.EAdBasicDrawable;
import ead.common.resources.assets.drawable.basics.Image;

/**
 * 
 * Represents a frame within a {@link FramesAnimation}
 * 
 */
public class Frame implements AssetDescriptor {

	/**
	 * Default frame time in milliseconds, with a value of 300 ms
	 */
	public static final int DEFAULT_FRAME_TIME = 300;

	/**
	 * Frame time in milliseconds
	 */
	@Param("time")
	private int time;

	@Param("drawable")
	private EAdBasicDrawable drawable;

	public Frame() {

	}

	/**
	 * Constructs a frame with the given image. Sets the frame time to
	 * {@link Frame#DEFAULT_FRAME_TIME}
	 * 
	 * @param uri
	 *            the uri to the image for the frame
	 */
	public Frame(String uri) {
		this(uri, DEFAULT_FRAME_TIME);
	}

	/**
	 * Constructs a frame with the given image and time.
	 * 
	 * @param uri
	 *            the uri to the image for the frame
	 * @param time
	 *            the time for the frame
	 */
	public Frame(String uri, int time) {
		this(new Image(uri), time);
	}

	public Frame(EAdBasicDrawable drawable, int time) {
		this.drawable = drawable;
		this.time = time;
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

	public EAdBasicDrawable getDrawable() {
		return drawable;
	}

	public boolean equals(Object o) {
		if (o instanceof Frame) {
			return super.equals(o) && ((Frame) o).time == time;
		}
		return false;
	}

	public void setDrawable(EAdBasicDrawable drawable) {
		this.drawable = drawable;
	}

}
