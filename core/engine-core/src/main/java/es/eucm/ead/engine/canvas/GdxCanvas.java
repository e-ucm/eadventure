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

package es.eucm.ead.engine.canvas;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.util.Matrix;
import es.eucm.ead.model.params.util.Rectangle;
import es.eucm.ead.engine.assets.fonts.RuntimeFont;

/**
 * <p>
 * Parametric canvas, which must be implemented for the different graphic
 * contexts (e.g. Graphics2D in Java)
 * </p>
 * 
 * @param <S>
 */
public class GdxCanvas extends SpriteBatch {

	private Stack<Matrix4> matrixes;

	private Matrix4 currentMatrix;

	public GdxCanvas() {
		matrixes = new Stack<Matrix4>();
		currentMatrix = new Matrix4();
		currentMatrix.set(new float[16]);
	}

	/**
	 * Creates a {@link Matrix4} compatible with Gdx equivalent to
	 * {@link EAdMatrix}
	 * 
	 * @param m
	 * @return
	 */
	public Matrix4 convertMatrix(Matrix m, Matrix4 dst) {
		float[] val = dst.getValues();

		float[] mat = m.getFlatMatrix();

		val[0] = mat[0];
		val[1] = mat[1];
		val[2] = mat[2];
		val[3] = 0;
		val[4] = mat[3];
		val[5] = mat[4];
		val[6] = mat[5];
		val[7] = 0;
		val[8] = 0;
		val[9] = 0;
		val[10] = 1;
		val[11] = 0;
		val[12] = mat[6];
		val[13] = mat[7];
		val[14] = 0;
		val[15] = mat[8];
		return dst;
	}

	public void drawText(String text, int x, int y, RuntimeFont font,
			EAdPaint paint) {
		y -= font.getBitmapFont().getAscent();
		x += font.getBitmapFont().getSpaceWidth() / 2;
		// Border
		if (paint.getBorder() instanceof ColorFill) {
			ColorFill c = (ColorFill) paint.getBorder();
			font.getBitmapFont().setColor(c.getRed() / 255.0f,
					c.getGreen() / 255.0f, c.getBlue() / 255.0f,
					getColor().a * c.getAlpha() / 255.0f);
			font.getBitmapFont().draw(this, text, x, y);
			font.getBitmapFont().draw(this, text, x + 1, y + 1);
			font.getBitmapFont().draw(this, text, x - 1, y + 1);
			font.getBitmapFont().draw(this, text, x + 1, y - 1);
			font.getBitmapFont().draw(this, text, x - 1, y - 1);
		}

		if (paint.getFill() instanceof ColorFill) {
			ColorFill c = (ColorFill) paint.getFill();
			font.getBitmapFont().setColor(c.getRed() / 255.0f,
					c.getGreen() / 255.0f, c.getBlue() / 255.0f,
					this.getColor().a * c.getAlpha() / 255.0f);
			font.getBitmapFont().draw(this, text, x, y);
		}
	}

	public void save() {
		matrixes.push(getTransformMatrix().cpy());
	}

	public void restore() {
		Matrix4 m = matrixes.pop();
		setTransformMatrix(m);
	}

	public void translate(int x, int y) {
		setTransformMatrix(getTransformMatrix().translate(x, y, 0));
	}

	public void scale(float scaleX, float scaleY) {
		setTransformMatrix(getTransformMatrix().scale(scaleX, scaleY, 1));
	}

	public void rotate(float angle) {
		setTransformMatrix(getTransformMatrix().rotate(0, 0, 1, angle));
	}

	/**
	 * Set the clipping rectangle on the canvas. The clip is used to limit the
	 * section of the canvas that needs to be draw.
	 * 
	 * @param rectangle
	 */
	// TODO clip with shapes to
	public void setClip(Rectangle rectangle) {

	}

}
