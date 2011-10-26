package es.eucm.eadventure.engine.core.util;

/**
 * Generic Java matrix definition, used for transformations
 */
public interface EAdMatrix {
	
	/**
	 * [0 3 6]
	 * [1 4 7]
	 * [2 5 8]
	 * 
	 * @return the matrix itself
	 */
	float[] getFlatMatrix();
	
	/**
	 * @return a transposed version of the values in the matrix
	 */
	float[] getTransposedMatrix();
	
	/**
	 * @return the inverse matrix
	 */
	float[] getInversedMatrix();
	
	void translate( float x, float y, boolean post );
	
	void rotate( float angle, boolean post );
	
	void scale( float scaleX, float scaleY, boolean post );
	
	void multiply( float m[], boolean post );
	
	float getOffsetX();
	
	float getOffsetY();
	
	void setIdentity();

	float[] multiplyPoint(float x, float y, boolean post);
	
	float[] multiplyPointInverse( float x, float y, boolean post);

}
