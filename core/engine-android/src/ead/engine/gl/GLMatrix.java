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

package ead.engine.gl;

import android.opengl.Matrix;
import ead.common.util.EAdMatrix;

public class GLMatrix implements EAdMatrix {

	private float m[];

	private float transposed[];

	private boolean transposedCalculated;

	private float inversed[];

	private boolean inversedCalculated;

	private boolean validated;

	public GLMatrix() {
		m = new float[16];
		transposed = new float[16];
		transposedCalculated = false;
		inversed = new float[16];
		inversedCalculated = false;
		Matrix.setIdentityM(m, 0);
		validated = false;
	}

	@Override
	public float[] getFlatMatrix() {
		return m;
	}

	@Override
	public float[] getTransposedMatrix() {
		if (!transposedCalculated) {
			Matrix.transposeM(transposed, 0, m, 0);
			transposedCalculated = true;
		}
		return transposed;
	}

	@Override
	public float[] getInversedMatrix() {
		if (!inversedCalculated) {
			Matrix.invertM(inversed, 0, m, 0);
			inversedCalculated = true;
		}
		return inversed;
	}

	@Override
	public void translate(float x, float y, boolean post) {
		Matrix.translateM(m, 0, x, y, 0);
		invalidateMatrixes();
	}

	@Override
	public void rotate(float angle, boolean post) {
		Matrix.rotateM(m, 0, angle, 0, 0, 1);
		invalidateMatrixes();
	}

	@Override
	public void scale(float scaleX, float scaleY, boolean post) {
		Matrix.scaleM(m, 0, scaleX, scaleY, 1);
		invalidateMatrixes();
	}

	@Override
	public void multiply(float[] m2, boolean post) {
		if (post) {
			Matrix.multiplyMM(m, 0, m, 0, m2, 0);
		} else {
			Matrix.multiplyMM(m, 0, m2, 0, m, 0);
		}
		invalidateMatrixes();
	}

	@Override
	public void setIdentity() {
		Matrix.setIdentityM(m, 0);
		invalidateMatrixes();

	}

	@Override
	public float[] multiplyPoint(float x, float y, boolean post) {
		float[] resultVec = new float[4];
		Matrix.multiplyMV(resultVec, 0, m, 0, new float[] { x, y, 0, 0 }, 0);
		return resultVec;
	}

	@Override
	public float[] multiplyPointInverse(float x, float y, boolean post) {
		float[] resultVec = new float[4];
		Matrix.multiplyMV(resultVec, 0, getInversedMatrix(), 0, new float[] {
				x, y, 0, 0 }, 0);
		return resultVec;
	}

	@Override
	public boolean isValidated() {
		return validated;
	}

	@Override
	public void setValidated(boolean validated) {
		this.validated = validated;

	}

	private void invalidateMatrixes() {
		transposedCalculated = false;
		inversedCalculated = false;
		validated = false;
	}

	public void setValues(float[] values) {
		if (values.length == 16) {
			m = values;
		} else {
			Matrix.setIdentityM(m, 0);
			m[0] = values[0];
			m[1] = values[1];
			m[3] = values[2];
			m[4] = values[3];
			m[5] = values[4];
			m[7] = values[5];
			m[12] = values[6];
			m[13] = values[7];
			m[15] = values[8];
		}
		invalidateMatrixes();
	}
}
