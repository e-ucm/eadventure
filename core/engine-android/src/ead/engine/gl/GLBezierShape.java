package ead.engine.gl;

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
import ead.common.util.EAdPosition;
import ead.engine.core.platform.assets.drawables.basics.RuntimeBezierShape;
import ead.engine.core.platform.rendering.AndroidCanvas;
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
		super.loadAsset();
		path = new Path();

		EAdPosition pos = descriptor.getPoints().get(0);
		path.moveTo(pos.getX(), pos.getY());

		int pointIndex = 1;
		EAdPosition p1, p2, p3;
		for (Integer i : descriptor.getSegmentsLength()) {
			switch (i) {
			case 1:
				p1 = descriptor.getPoints().get(pointIndex++);
				path.lineTo(p1.getX(), p1.getY());
				break;
			case 2:
				p1 = descriptor.getPoints().get(pointIndex++);
				p2 = descriptor.getPoints().get(pointIndex++);
				path.quadTo(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				break;
			case 3:
				p1 = descriptor.getPoints().get(pointIndex++);
				p2 = descriptor.getPoints().get(pointIndex++);
				p3 = descriptor.getPoints().get(pointIndex++);
				path.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY());
				break;
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
