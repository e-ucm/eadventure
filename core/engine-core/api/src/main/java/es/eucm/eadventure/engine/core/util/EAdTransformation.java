package es.eucm.eadventure.engine.core.util;

/**
 * Transformation applicable to the graphic context.
 * <p>
 * Transformation includes a transformation matrix (translation, rotation and
 * scale) as well as values for visibility and alpha.
 */
public interface EAdTransformation extends Cloneable {

	/**
	 * @return the transformation matrix
	 */
	EAdMatrix getMatrix();

	/**
	 * @return true if the transformed elements must be visible
	 */
	boolean isVisible();

	/**
	 * @return the transparency of the transformation (1.0 opaque, 0.0 transparent)
	 */
	float getAlpha();

	/**
	 * @return clone of the transformation
	 */
	Object clone();

}
