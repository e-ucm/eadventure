package es.eucm.eadventure.engine.core;

import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public interface Renderable {
	
	void doLayout( EAdTransformation t );
	
	void render( EAdCanvas<?> c );
	
	void update();
	
	boolean contains( int x, int y );
	
	boolean processAction(GUIAction action);
	
	EAdTransformation getTransformation();

}
