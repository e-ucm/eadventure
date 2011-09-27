package es.eucm.eadventure.engine.core.util.impl;

import es.eucm.eadventure.engine.core.util.EAdMatrix;

public class EAdMatrixImpl implements EAdMatrix {

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
	private float[] m;

	/**
	 * Constructs a matrix 3x3 with the identity
	 */
	public EAdMatrixImpl() {
		m = new float[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 };
	}

	public EAdMatrixImpl(float m[]) {
		this.m = m;
	}

	@Override
	public float[] getFlatMatrix() {
		return m;
	}

	@Override
	public void translate(float x, float y) {
		float t[] = getIdentity();
		t[6] = x;
		t[7] = y;
		postMultiply(t);
	}

	@Override
	public void rotate(float angle) {
		float r[] = getIdentity();
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		r[0] = cos;
		r[1] = sin;
		r[3] = -sin;
		r[4] = cos;
		postMultiply(r);
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		float s[] = getIdentity();
		s[0] = scaleX;
		s[4] = scaleY;
		postMultiply(s);
	}

	@Override
	public void preMultiply(float m1[]) {
		this.m = multiply(m1, this.m);

	}

	@Override
	public void postMultiply(float m1[]) {
		this.m = multiply(this.m, m1);

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

}
