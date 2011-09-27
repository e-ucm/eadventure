package es.eucm.eadventure.engine.core.util;

public interface EAdMatrix {
	
	float[] getFlatMatrix();
	
	void translate( float x, float y );
	
	void rotate( float angle );
	
	void scale( float scaleX, float scaleY );
	
	void preMultiply( float m[] );
	
	void postMultiply( float m[] );

}
