package es.eucm.eadventure.common.resources.assets.drawable.filters;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.drawable.filters.DrawableFilter;
import es.eucm.eadventure.common.util.EAdMatrix;

public class MatrixFilter implements DrawableFilter {

	@Param("matrix")
	private EAdMatrix matrix;

	@Param("originX")
	private float originX;

	@Param("originY")
	private float originY;

	public MatrixFilter() {
	}
	
	/**
	 * 
	 * @param m
	 *            the matrix
	 * @param originX
	 *            the origin, a value between 0 and 1, 0 meaning 0 as x
	 *            coordinate, and 1 meaning drawable width as x coordinate
	 * @param originY
	 * the origin, a value between 0 and 1, 0 meaning 0 as y
	 *            coordinate, and 1 meaning drawable height as y coordinate
	 */
	public MatrixFilter(EAdMatrix m, float originX, float originY) {
		this.matrix = m;
		this.originX = originX;
		this.originY = originY;
	}

	public MatrixFilter(EAdMatrix m) {
		this(m, 0.5f, 0.5f);
	}

	public float getOriginX() {
		return originX;
	}

	public float getOriginY() {
		return originY;
	}

	public EAdMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(EAdMatrix matrix) {
		this.matrix = matrix;
	}

	public void setOriginX(float originX) {
		this.originX = originX;
	}

	public void setOriginY(float originY) {
		this.originY = originY;
	}
	
	

}
