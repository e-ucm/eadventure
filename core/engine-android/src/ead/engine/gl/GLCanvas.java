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

import java.nio.Buffer;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.resources.assets.text.EAdFont;
import ead.common.util.EAdMatrix;
import ead.common.util.EAdRectangle;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.AbstractCanvas;
import ead.engine.core.util.EAdTransformation;

@Singleton
public class GLCanvas extends AbstractCanvas<GL10> {

	private float[] projectionMatrix;

	private Stack<GLMatrix> matrixesStack;

	private float[] mvpMatrix;

	private int vertexShaderHandle;

	private int fragmentShaderHandle;

	private int programHandle;

	private GLAssetHandler assetHandler;

	private int matrixHandle;

	private int positionHandle;

	private int colorHandle;

	private int textureCoordinatesHandle;

	private int textureHandle;

	private int alphaHandle;

	public static final String POSITION = "a_Position";

	public static final String COLOR = "a_Color";

	public static final String TEXTURE = "a_TexCoordinate";
	
	public static final String ALPHA = "u_Alpha";

	@Inject
	public GLCanvas(FontHandler fontHandler, GLAssetHandler assetHandler) {
		super(fontHandler, null);
		this.assetHandler = assetHandler;

	}

	public void init() {
		projectionMatrix = new float[16];
		mvpMatrix = new float[16];
		// FIXME id doesn't have to be 800x600
		Matrix.orthoM(projectionMatrix, 0, 0, 800, 600, 0, 1, -1);

		matrixesStack = new Stack<GLMatrix>();
		matrixesStack.push(new GLMatrix());

		vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

		if (vertexShaderHandle != 0) {
			// Shader source
			StringBuilder b = assetHandler
					.getTextFromFile("shaders/default.vertex");
			GLES20.glShaderSource(vertexShaderHandle, b.toString());
			GLES20.glCompileShader(vertexShaderHandle);

			int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS,
					compileStatus, 0);
			if (compileStatus[0] == 0) {
				logger.warn("{}", GLES20.glGetShaderInfoLog(vertexShaderHandle));
			}
		}

		fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		if (fragmentShaderHandle != 0) {
			// Shader source
			StringBuilder b = assetHandler
					.getTextFromFile("shaders/default.fragment");
			GLES20.glShaderSource(fragmentShaderHandle, b.toString());
			GLES20.glCompileShader(fragmentShaderHandle);

			int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(fragmentShaderHandle,
					GLES20.GL_COMPILE_STATUS, compileStatus, 0);
			if (compileStatus[0] == 0) {
				logger.warn("{}",
						GLES20.glGetShaderInfoLog(fragmentShaderHandle));
			}
		}

		programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) {
			GLES20.glAttachShader(programHandle, vertexShaderHandle);
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			GLES20.glBindAttribLocation(programHandle, 0, POSITION);
			GLES20.glBindAttribLocation(programHandle, 1, COLOR);
			GLES20.glBindAttribLocation(programHandle, 2, TEXTURE);

			GLES20.glLinkProgram(programHandle);

			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS,
					linkStatus, 0);

			if (linkStatus[0] == 0) {
				logger.warn("Shader not loaded {}",
						GLES20.glGetProgramInfoLog(programHandle));
			}
		}

		// Set program handles. These will later be used to pass in values to
		// the program.
		matrixHandle = GLES20
				.glGetUniformLocation(programHandle, "u_MVPMatrix");
		
		alphaHandle = GLES20.glGetUniformLocation(programHandle, ALPHA);
		textureHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
		
		positionHandle = GLES20.glGetAttribLocation(programHandle, POSITION);
		colorHandle = GLES20.glGetAttribLocation(programHandle, COLOR);
		textureCoordinatesHandle = GLES20.glGetAttribLocation(programHandle,
				TEXTURE);

		GLES20.glUseProgram(programHandle);
	}

	@Override
	public void setTransformation(EAdTransformation t) {
		setMatrix(t.getMatrix());
		GLES20.glUniform1f(alphaHandle, t.getAlpha());
	}

	@Override
	public void setMatrix(EAdMatrix m) {
		matrixesStack.peek().setValues(m.getFlatMatrix());
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, matrixesStack.peek().getFlatMatrix(), 0);
		GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0);
	}

	@Override
	public void drawShape(RuntimeDrawable<? extends EAdShape, GL10> shape) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawText(String text, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFont(EAdFont font) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clip(EAdRectangle rectangle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		GLMatrix m = new GLMatrix();
		m.setValues(matrixesStack.peek().getFlatMatrix());
		matrixesStack.push(m);
	}

	@Override
	public void restore() {
		matrixesStack.pop();
	}

	@Override
	public void translate(int x, int y) {
		matrixesStack.peek().translate(x, y, false);

	}

	@Override
	public void scale(float scaleX, float scaleY) {
		matrixesStack.peek().scale(scaleX, scaleY, false);

	}

	@Override
	public void rotate(float angle) {
		matrixesStack.peek().rotate(angle, false);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {

	}

	public void setAttributePosition(Buffer buffer) {
		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
				0, buffer);
		GLES20.glEnableVertexAttribArray(positionHandle);
	}

	public void setAttributeColor(Buffer buffer) {
		GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0,
				buffer);
		GLES20.glEnableVertexAttribArray(colorHandle);
	}

	public void setAttributeTexture(Buffer buffer, int texture) {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		GLES20.glUniform1i(textureHandle, 0);
		GLES20.glVertexAttribPointer(textureCoordinatesHandle, 2,
				GLES20.GL_FLOAT, false, 0, buffer);
		GLES20.glEnableVertexAttribArray(textureCoordinatesHandle);
	}

}
