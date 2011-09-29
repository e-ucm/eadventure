package es.eucm.eadventure.engine.core.util;



public interface EAdMatrix {
	
	float[] getFlatMatrix();
	
	void postTranslate( float x, float y );
	
	void preTranslate( float x, float y );
	
	void postRotate( float angle );
	
	void preRotate( float angle );
	
	void postScale( float scaleX, float scaleY );
	
	void preScale( float scaleX, float scaleY );
	
	void preMultiply( float m[] );
	
	void postMultiply( float m[] );
	
	float getOffsetX();
	
	float getOffsetY();
	
	void setIdentity();

	float[] postMultiplyPoint(float x, float y);
	
	float[] preMultiplyPoint( float x, float y);

}
