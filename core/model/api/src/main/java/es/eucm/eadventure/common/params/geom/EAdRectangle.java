package es.eucm.eadventure.common.params.geom;

import es.eucm.eadventure.common.params.EAdParam;

/**
 * General interface to represent eAdventure rectangles
 * 
 */
public interface EAdRectangle extends EAdParam {

	/**
	 * Returns the x top left coordinate
	 * 
	 * @return
	 */
	int getX();

	/**
	 * Returns the y top left coordinate
	 * 
	 * @return
	 */
	int getY();

	/**
	 * Returns the width of the rectangle
	 * 
	 * @return
	 */
	int getWidth();

	/**
	 * Returns the height of the rectangle
	 * 
	 * @return
	 */
	int getHeight();

	/**
	 * Returns <b>true</b> if the given point is contained by this rectangle;
	 * <b>false</b> otherwise
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return <b>true</b> if the given point is contained by this rectangle;
	 *         <b>false</b> otherwise
	 */
	boolean contains(int x, int y);

}
