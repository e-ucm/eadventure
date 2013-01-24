package ead.common.model.assets.drawable.filters;

import ead.common.interfaces.Param;
import ead.common.model.assets.AbstractAssetDescriptor;
import ead.common.model.assets.drawable.filters.EAdDrawableFilter;
import ead.common.model.params.util.EAdMatrix;

public class MatrixFilter extends AbstractAssetDescriptor implements
		EAdDrawableFilter {

	@Param
	private EAdMatrix matrix;

	@Param
	private float originX;

	@Param
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
