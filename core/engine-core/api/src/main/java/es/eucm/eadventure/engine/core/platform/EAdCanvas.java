package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public interface EAdCanvas<S> {
	
	void setGraphicContext( S g );

	void setTransformation(EAdTransformation t);

	void drawImage(DrawableAsset<? extends Image> image);

	void drawShape(DrawableAsset<? extends Shape> shape);

	/**
	 * Draws the text in 0, 0. Same result can be accomplished calling
	 * {@link EAdCanvas#drawText(String, int, int)} with (0, 0)
	 * 
	 * @param text the text to draw
	 */
	void drawText(String text);

	void drawText(String text, int x, int y);

	void setPaint(EAdPaint paint);

	void setFont(EAdFont font);

	void clip(EAdRectangle rectangle);

	void save();

	void restore();

	S getNativeGraphicContext();

	void translate(int x, int y);

}
