package es.eucm.eadventure.common.params.geom;

import es.eucm.eadventure.common.params.EAdParam;

public interface EAdRectangle extends EAdParam {
	
	int getX();
	
	int getY();
	
	int getWidth();
	
	int getHeight();
	
	boolean contains( int x, int y );

}
