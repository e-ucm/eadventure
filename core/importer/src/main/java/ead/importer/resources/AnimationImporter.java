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

package ead.importer.resources;

import com.google.inject.Inject;

import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.importer.GenericImporter;
import es.eucm.eadventure.common.data.animation.Animation;

public class AnimationImporter implements
		GenericImporter<Animation, FramesAnimation> {

	private GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame> frameImporter;

	@Inject
	public AnimationImporter(
			GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame> frameImporter) {
		this.frameImporter = frameImporter;
	}

	@Override
	public FramesAnimation convert(Animation oldObject, Object object) {
		FramesAnimation frames = (FramesAnimation) object;
		// FIXME Transitions in animations not imported
		for (es.eucm.eadventure.common.data.animation.Frame f : oldObject
				.getFrames()) {
			Frame newFrame = frameImporter.init(f);
			newFrame = frameImporter.convert(f, newFrame);
			if (newFrame != null)
				frames.addFrame(newFrame);
		}

		return frames;
	}

	@Override
	public FramesAnimation init(Animation oldObject) {
		return new FramesAnimation();
	}

}
