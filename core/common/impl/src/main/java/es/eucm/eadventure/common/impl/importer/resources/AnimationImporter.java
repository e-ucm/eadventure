package es.eucm.eadventure.common.impl.importer.resources;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;

public class AnimationImporter implements Importer<Animation, FramesAnimation> {

	private Importer<es.eucm.eadventure.common.data.animation.Frame, Frame> frameImporter;

	@Inject
	public AnimationImporter(
			Importer<es.eucm.eadventure.common.data.animation.Frame, Frame> frameImporter) {
		this.frameImporter = frameImporter;
	}

	@Override
	public FramesAnimation convert(Animation oldObject) {
		FramesAnimation frames = new FramesAnimation();
		// FIXME Transitions in animations not imported
		for (es.eucm.eadventure.common.data.animation.Frame f : oldObject
				.getFrames()) {
			Frame newFrame = frameImporter.convert(f);
			if (newFrame != null)
				frames.addFrame(newFrame);
		}

		return frames;
	}

	@Override
	public boolean equals(Animation oldObject, FramesAnimation newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
