package es.eucm.eadventure.engine;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;

import com.google.inject.Inject;

import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.engine.assets.AndroidBezierShape;
import es.eucm.eadventure.engine.assets.AndroidEngineImage;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.impl.AbstractCanvas;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class AndroidCanvas extends AbstractCanvas<Canvas> {

	private Map<EAdFill, Paint> fillCache;

	@Inject
	public AndroidCanvas(FontHandler fontHandler) {
		super(fontHandler);
		fillCache = new HashMap<EAdFill, Paint>();
	}

	@Override
	public void setTransformation(EAdTransformation t) {
		
		g.restore();
		
		float m[] = t.getMatrix().getTransposedMatrix();
		Matrix matrix = new Matrix();
		matrix.setValues(m);
		matrix.postConcat(g.getMatrix());
		
		g.save();		
		g.setMatrix(matrix);

		// TODO alpha
	}

	@Override
	public void drawImage(DrawableAsset<? extends Image> image) {
		g.drawBitmap(((AndroidEngineImage) image).getImage(), 0, 0, null);
	}

	@Override
	public void drawShape(DrawableAsset<? extends Shape> shape) {
		Path s = ((AndroidBezierShape) shape).getShape();
		// Fill
		if (paint.getFill() != null) {
			Paint p = getPaint(paint.getFill());
			p.setStyle(Style.FILL);
			g.drawPath(s, p);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			Paint p = getPaint(paint.getBorder());
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(paint.getBorderWidth());
			g.drawPath(s, p);
		}
	}

	@Override
	public void drawText(String str, int x, int y) {
		// Fill
		if (paint.getFill() != null) {
			Paint p = getPaint(paint.getFill());
			p.setStyle(Style.FILL);
			g.drawText(str, x, y, p);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			Paint p = getPaint(paint.getBorder());
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(paint.getBorderWidth());
			g.drawText(str, x, y, p);
		}
	}

	@Override
	public void setFont(EAdFont font) {
		// TODO fonts in android
	}

	@Override
	public void clip(EAdRectangle rectangle) {
		g.clipRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}

	private Paint getPaint(EAdFill fill) {
		Paint p = fillCache.get(fill);
		if (p == null) {
			p = new Paint();
			if (fill instanceof EAdColor) {
				EAdColor c = (EAdColor) fill;
				p.setColor(Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
						c.getBlue()));

			} else if (fill instanceof EAdLinearGradient) {
				EAdLinearGradient gradient = (EAdLinearGradient) fill;
				EAdColor c = gradient.getColor1();
				int c1 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
						c.getBlue());
				c = gradient.getColor2();
				int c2 = Color.argb(c.getAlpha(), c.getRed(), c.getGreen(),
						c.getBlue());
				p.setShader(new LinearGradient(gradient.getX0(), gradient
						.getY1(), gradient.getX1(), gradient.getY0(), c1, c2,
						Shader.TileMode.REPEAT));
			}
			fillCache.put(fill, p);
		}
		return p;
	}

	@Override
	public void save() {
		g.save();
	}

	@Override
	public void restore() {
		g.restore();
	}

	@Override
	public void translate(int x, int y) {
		g.translate(x, y);
	}

}
