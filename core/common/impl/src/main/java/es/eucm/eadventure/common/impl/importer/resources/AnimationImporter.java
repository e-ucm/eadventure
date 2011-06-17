package es.eucm.eadventure.common.impl.importer.resources;

import com.google.inject.Inject;

import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;

public class AnimationImporter implements GenericImporter<Animation, FramesAnimation> {

	private GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame> frameImporter;

	@Inject
	public AnimationImporter(
			GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame> frameImporter) {
		this.frameImporter = frameImporter;
	}

	@Override
	public FramesAnimation convert(Animation oldObject, Object object ) {
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
