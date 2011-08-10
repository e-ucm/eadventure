package es.eucm.eadventure.common.params.geom;

import es.eucm.eadventure.common.params.EAdParam;

/**
 * General interface for eAdventure positions
 * 
 */
public interface EAdPosition extends EAdParam {

	/**
	 * @return the x coordinate
	 */
	int getX();

	/**
	 * @return the y coordinate
	 */
	int getY();

	/**
	 * Return the value of x in java coordinate system given a width. The
	 * calculation done is {@code x - dispX * width}
	 * 
	 * @param width
	 *            The width of the element
	 * @return the x value in the java coordinate system
	 */
	int getJavaX(float width);

	/**
	 * Return the value of y in java coordinate system given a height. The
	 * calculation done is {@code y - dispY * height}
	 * 
	 * @param height
	 *            The height of the element
	 * @return the y value in the java coordinate system
	 */
	int getJavaY(float height);

	/**
	 * Sets the x coordinate
	 * 
	 * @param x
	 *            the x to set
	 */
	void setX(int x);

	/**
	 * Sets the y coordinate
	 * 
	 * @param y
	 *            the y to set
	 */
	void setY(int y);

	/**
	 * Returns the displacement in the x axis
	 * 
	 * @return
	 */
	float getDispX();

	/**
	 * Returns the displacement in the y axis
	 * 
	 * @return
	 */
	float getDispY();

	/**
	 * Sets x and y coordinates for this position
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	void set(int x, int y);

}
