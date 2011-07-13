package es.eucm.eadventure.common.elmentfactories.assets;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.OrientedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.StateDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.OrientedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.StateDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class DrawableFactory {
	
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
	
	public OrientedDrawable getOrientedDrawable( String[] uris ){
		OrientedDrawableImpl oriented = new OrientedDrawableImpl( );
		if ( uris.length == 4 ){
			int i = 0;
			oriented.setDrawable(Orientation.N, getImage( uris[i++]));
			oriented.setDrawable(Orientation.E, getImage( uris[i++]));
			oriented.setDrawable(Orientation.S, getImage( uris[i++]));
			oriented.setDrawable(Orientation.W, getImage( uris[i++]));
		}
		return oriented;
	}
	
	public StateDrawable getStateDrawable( String[] states, Drawable[] drawables){
		StateDrawableImpl state = new StateDrawableImpl( );
		if ( states.length == drawables.length ){
			int i = 0;
			for ( String s: states ){
				state.addDrawable(s, drawables[i++]);
			}
		}
		return state;
	}
	

}
