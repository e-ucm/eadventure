package es.eucm.eadventure.common.elmentfactories.assets;

import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class ImageFactory {
	
	public Image getImage( String uri ){
		Image image = new ImageImpl( uri );
		return image;
	}
	
	public FramesAnimation getFramesAnimation( String[] uris, int timePerFrame ){
		FramesAnimation frames = new FramesAnimation();
		for ( String s: uris ){
			frames.addFrame(new Frame( s, timePerFrame ));
		}
		return frames;
		
	}

}
