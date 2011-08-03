package es.eucm.eadventure.common.params.geom;

import es.eucm.eadventure.common.params.EAdParam;

public interface EAdPosition extends EAdParam {
	
	int getX();
	
	int getY();
	
	int getJavaX( float width );
	
	int getJavaY( float height );

	void setX(int x);
	
	void setY(int y);

	float getDispX();

}
