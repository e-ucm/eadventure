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

package ead.engine.core.util;

import ead.common.util.EAdMatrix;
import ead.common.util.BasicMatrix;
import ead.common.util.EAdRectangle;
import ead.engine.core.util.EAdTransformation;

public class EAdTransformationImpl implements EAdTransformation {

	public static final EAdTransformation INITIAL_TRANSFORMATION = new EAdTransformationImpl();

	private EAdMatrix matrix;

	private boolean visible;

	private float alpha;

	private boolean validated;

	public EAdRectangle clip;

	public EAdTransformationImpl(EAdMatrix matrix, boolean visible, float alpha) {
		this.matrix = matrix;
		this.visible = visible;
		this.alpha = alpha;
		validated = false;
	}

	public EAdTransformationImpl() {
		matrix = new BasicMatrix();
		visible = true;
		alpha = 1.0f;
		validated = false;
	}

	@Override
	public EAdMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(EAdMatrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		if (alpha != this.alpha) {
			validated = false;
			this.alpha = alpha;
		}
	}

	public void setVisible(boolean visible) {
		if (visible != this.visible) {
			validated = false;
			this.visible = visible;
		}
	}

	public Object clone() {
		EAdTransformationImpl t = new EAdTransformationImpl();
		t.alpha = alpha;
		t.visible = visible;
		t.matrix = new BasicMatrix(matrix.getFlatMatrix());
		return t;

	}

	public EAdRectangle getClip() {
		return clip;
	}

	public void setClip(int x, int y, int width, int height) {
		validated = false;
		if (clip == null) {
			clip = new EAdRectangle(x, y, width, height);
		} else {
			clip.x = x;
			clip.y = y;
			clip.width = width;
			clip.height = height;
		}
	}

	@Override
	public boolean isValidated() {
		return validated && matrix.isValidated();
	}

	@Override
	public void setValidated(boolean validated) {
		this.validated = validated;
		matrix.setValidated(validated);
	}

}
