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

package es.eucm.eadventure.common.elmentfactories.assets;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.OrientedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.StateDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.OrientedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.StateDrawableImpl;

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
