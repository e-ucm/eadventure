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

package ead.engine.android.gl;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.opengl.GLES20;

import com.google.inject.Inject;

import ead.common.params.paint.EAdPaint;
import ead.engine.android.core.platform.rendering.AndroidCanvas;
import ead.engine.core.platform.assets.drawables.basics.RuntimeBezierShape;
import ead.engine.core.platform.rendering.GenericCanvas;

public class GLBezierShape extends RuntimeBezierShape<GL10>{
	
	private Path path;
	
	private GLAssetHandler assetHandler;
	
	private FloatBuffer buffer;

	private FloatBuffer colorBuffer;

	private FloatBuffer textureBuffer;

	private int textureId = -1;

	private int position;

	private RectF bounds;
	
	@Inject
	public GLBezierShape(GLAssetHandler assetHandler){
		this.assetHandler = assetHandler;
	}
	
	public boolean loadAsset(){
		// FIXME support paint as vector image
		super.loadAsset();
		path = new Path();

		int pointIndex = 2;
		float x1, y1, x2, y2, x3, y3;

		while (pointIndex < descriptor.getPoints().size()) {
			int length = descriptor.getPoints().get(pointIndex++);
			switch (length) {
			case 1:
				x1 = descriptor.getPoints().get(pointIndex++);
				y1 = descriptor.getPoints().get(pointIndex++);
				path.lineTo(x1, y1);
				break;
			case 2:
				x1 = descriptor.getPoints().get(pointIndex++);
				y1 = descriptor.getPoints().get(pointIndex++);
				x2 = descriptor.getPoints().get(pointIndex++);
				y2 = descriptor.getPoints().get(pointIndex++);
				path.quadTo(x1, y1, x2, y2);
				break;
			case 3:
				x1 = descriptor.getPoints().get(pointIndex++);
				y1 = descriptor.getPoints().get(pointIndex++);
				x2 = descriptor.getPoints().get(pointIndex++);
				y2 = descriptor.getPoints().get(pointIndex++);
				x3 = descriptor.getPoints().get(pointIndex++);
				y3 = descriptor.getPoints().get(pointIndex++);
				path.cubicTo(x1, y1, x2, y2, x3, y3);
				break;
			default:

			}
		}

		if (descriptor.isClosed())
			path.close();
		
		bounds = new RectF();
		path.computeBounds(bounds, true);
		this.width = (int) bounds.right;
		this.height = (int) bounds.bottom;
		
		Bitmap b = Bitmap.createBitmap((int) bounds.right, (int) bounds.bottom, Config.ARGB_8888 );
		Canvas c = new Canvas( b );
		
		EAdPaint paint = descriptor.getPaint();
		// Fill
		if (paint.getFill() != null) {
			Paint p = AndroidCanvas.createPaint(paint.getFill());
			p.setStyle(Style.FILL);
			c.drawPath(path, p);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			Paint p = AndroidCanvas.createPaint(paint.getBorder());
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(paint.getBorderWidth());
			c.drawPath(path, p);
		}
		
		((GLAssetHandler) assetHandler).addTexture(descriptor, b);
		b.recycle();
		buffer = ((GLAssetHandler) assetHandler).getFloatBuffer(width,
				height);
		float[] color = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
		colorBuffer = ((GLAssetHandler) assetHandler).getFloatBuffer(color);

		return true;
	}

	@Override
	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}

	@Override
	public void freeMemory() {
		
	}
	
	@Override
	public void render(GenericCanvas<GL10> c) {
		if (buffer != null) {
			GLCanvas gl = (GLCanvas) c;
			buffer.position(0);
			gl.setAttributePosition(buffer);
			colorBuffer.position(0);
			gl.setAttributeColor(colorBuffer);

			if (textureId == -1) {
				int index = ((GLAssetHandler) assetHandler)
						.getBitmapTextureIndex(descriptor);
				textureBuffer = ((GLAssetHandler) assetHandler)
						.getBuffer(index);
				textureId = ((GLAssetHandler) assetHandler).getTextureId(index);
				position = ((GLAssetHandler) assetHandler).getPosition(index,
						descriptor);
			}

			textureBuffer.position(position);
			gl.setAttributeTexture(textureBuffer, textureId);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
		}
	}

}
