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
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.resources.assets.drawable.EAdDrawable;

/**
 * Represents a frames animation. Contains frames
 * 
 */
public class FramesAnimation implements EAdDrawable {

	@Param("frames")
	private EAdList<Frame> frames;

	/**
	 * Constructs an empty animation
	 */
	public FramesAnimation() {
		frames = new EAdListImpl<Frame>(Frame.class);
	}

	/**
	 * Adds a frame to the and of the animation
	 * 
	 * @param frame
	 */
	public void addFrame(Frame frame) {
		frames.add(frame);
	}
	
	private int getTotalTime() {
		int totalTime = 0;
		for (Frame frame : frames)
			totalTime += frame.getTime();
		return totalTime;
	}

	/**
	 * Returns the frame situated at the given index
	 * 
	 * @param index
	 *            index
	 * @return the frame at the index
	 */
	public Frame getFrame(int index) {
		return frames.get(index);
	}

	/**
	 * Returns the total number of frames of this animation
	 * 
	 * @return the number of frames
	 */
	public int getFrameCount() {
		return frames.size();
	}

	public Frame getFrameFromTime(long timeDisplayed) {
		int totalTime = getTotalTime();
		if (totalTime > 0) {
			long time = timeDisplayed % totalTime;

			int i = 0;
			while (time > getFrame(i).getTime()) {
				time -= getFrame(i).getTime();
				i++;
			}

			return getFrame(i);
		}
		return null;
	}

	public EAdList<Frame> getFrames() {
		return frames;
	}
	
	
	
}
