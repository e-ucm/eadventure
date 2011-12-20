/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.engine.core.util.impl;

import es.eucm.eadventure.common.util.EAdMatrix;

/**
 * Default {@link EAdMatrix} implementation
 */
public class EAdMatrixImpl implements EAdMatrix {

	/**
	 * Dimension of the transformation matrix
	 */
	private static final int DIMENSION = 3;

	/**
	 * <p>
	 * [ m0 m3 m6 ]
	 * </p>
	 * <p>
	 * [ m1 m4 m7 ]
	 * </p>
	 * <p>
	 * [ m2 m5 m8 ]
	 * </p>
	 */
	private float[] matrix;

	/**
	 * Inverse matrix, can be invalidated
	 */
	private float[] inverse;
	
	/**
	 * Transposed matrix, can be invalidated
	 */
	private float[] transposed;

	/**
	 * Constructs a matrix 3x3 with the identity
	 */
	public EAdMatrixImpl() {
		invalidateMatrixes();
		matrix = getIdentity();
	}

	public EAdMatrixImpl(float m[]) {
		this.matrix = m;
	}

	@Override
	public float[] getFlatMatrix() {
		return matrix;
	}

	@Override
	public float[] getTransposedMatrix() {
		if (transposed == null) {
			transposed = new float[9];
			transposed[0] = matrix[0];
			transposed[1] = matrix[3];
			transposed[2] = matrix[6];
			transposed[3] = matrix[1];
			transposed[4] = matrix[4];
			transposed[5] = matrix[7];
			transposed[6] = matrix[2];
			transposed[7] = matrix[5];
			transposed[8] = matrix[8];
		}
		return transposed;
	}

	@Override
	public void translate(float x, float y, boolean post) {
		float t[] = getIdentity();
		t[6] = x;
		t[7] = y;
		multiply(t, post);
	}

	@Override
	public void rotate(float angle, boolean post) {
		multiply(getRotationMatrix(angle), post);
	}

	private float[] getRotationMatrix(float angle) {
		float r[] = getIdentity();
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		r[0] = cos;
		r[1] = sin;
		r[3] = -sin;
		r[4] = cos;
		return r;
	}

	@Override
	public void scale(float scaleX, float scaleY, boolean post) {
		invalidateMatrixes();
		float s[] = getIdentity();
		s[0] = scaleX;
		s[4] = scaleY;
		multiply(s, post);
	}

	@Override
	public void multiply(float m1[], boolean post) {
		invalidateMatrixes();
		if (post)
			this.matrix = multiply(this.matrix, m1);
		else
			this.matrix = multiply(m1, this.matrix);
	}

	public static float[] multiply(float[] m1, float[] m2) {
		float m[] = new float[DIMENSION * DIMENSION];
		int row = 0, column = 0;
		
		for (int i = 0; i < DIMENSION * DIMENSION; i++) {
			row = i % DIMENSION;
			column = i / DIMENSION;
			m[i] = 0;
			for (int j = 0; j < DIMENSION; j++) {
				m[i] += m1[row + j * DIMENSION] * m2[column * DIMENSION + j];
			}
		}
		return m;
	}

	public static float[] getIdentity() {
		return new float[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 };
	}

	public float getOffsetX() {
		return matrix[7];
	}

	public float getOffsetY() {
		return matrix[8];
	}

	public void setIdentity() {
		invalidateMatrixes();
		matrix = getIdentity();
	}

	@Override
	public float[] multiplyPoint(float x, float y, boolean post) {
		return multiplyPoint(matrix, x, y, post);
	}
	
	private float[] multiplyPoint(float m[], float x, float y, boolean post) {
		float px, py;
		if (post) {
			px = m[0] * x + m[3] * y + m[6];
			py = m[1] * x + m[4] * y + m[7];
		} else {
			px = m[0] * x + m[1] * y + m[2];
			py = m[3] * x + m[4] * y + m[5];
		}
		return new float[] { px, py };
	}

	@Override
	public float[] multiplyPointInverse(float x, float y, boolean post) {
		return multiplyPoint(getInversedMatrix(), x, y, post);
	}

	public float[] getInversedMatrix() {
		recalculate();
		return inverse;
	}
	
	private void invalidateMatrixes() {
		inverse = null;
		transposed = null;
	}
	
	private void recalculate() {
		if (inverse == null) {
			inverse = new float[9];
			inverse[2] = 0;
			inverse[5] = 0;
			inverse[8] = 1;
			float det = matrix[0] * matrix[4] - matrix[3] * matrix[1];
			inverse[0] = matrix[4] / det;
			inverse[1] = - matrix[1] / det;
			inverse[3] = - matrix[3] / det;
			inverse[4] = matrix[0] / det;

			inverse[6] = (-inverse[0] * matrix[6] - inverse[3] * matrix[7]);
			inverse[7] = (-inverse[1] * matrix[6] - inverse[4] * matrix[7]);
		}
	}

}
