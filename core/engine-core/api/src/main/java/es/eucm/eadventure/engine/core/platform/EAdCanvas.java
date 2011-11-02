package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

/**
 * <p>
 * Parametric canvas, which must be implemented for the different graphic
 * contexts (e.g. Graphics2D in Java)
 * </p>
 * 
 * @param <S>
 */
public interface EAdCanvas<S> {

	/**
	 * @param g
	 *            The graphic context where elements are rendered
	 */
	void setGraphicContext(S g);

	/**
	 * @param t
	 *            The transformation to be applied to elements rendered in the
	 *            canvas
	 */
	void setTransformation(EAdTransformation t);

	/**
	 * @param image
	 *            Draw an image in the graphic context (transformations
	 *            configured through 
	 *            {@code setTransformation(EAdTransformation t)} are applied)
	 */
	void drawImage(DrawableAsset<? extends Image> image);

	/**
	 * @param shape
	 *            Draw a shape in the graphic context (transformations
	 *            configured through
	 *            {@code setTransformation(EAdTransformation t)} are applied)
	 */
	void drawShape(DrawableAsset<? extends Shape> shape);

	/**
	 * Draws the text in 0, 0. Same result can be accomplished calling
	 * {@link EAdCanvas#drawText(String, int, int)} with (0, 0)
	 * 
	 * @param text
	 *            the text to draw
	 */
	void drawText(String text);

	/**
	 * Draws the text in a displaced position.
	 * 
	 * @param text
	 *            the text to draw
	 * @param x
	 *            position along the x axis
	 * @param y
	 *            position along the y axis
	 */
	void drawText(String text, int x, int y);

	/**
	 * Set the paint to be used in elements rendered to the graphic context.
	 * Paint applies to shapes and text.
	 * 
	 * @param paint
	 *            The {@link EAdPaint} to be used
	 */
	void setPaint(EAdPaint paint);

	/**
	 * Set the font to be used to render text in the graphic context.
	 * 
	 * @param font
	 *            The {@link EAdFont} to be used
	 */
	void setFont(EAdFont font);

	/**
	 * Set the clipping rectangle on the canvas. The clip is used to limit the
	 * section of the canvas that needs to be draw.
	 * 
	 * @param rectangle
	 */
	// TODO Remove?
	void clip(EAdRectangle rectangle);

	/**
	 * Save the configuration and parameters of the graphic context in a stack
	 */
	void save();

	/**
	 * Restore the latest configuration and parameters of the graphic context
	 * saved to the stack
	 */
	void restore();

	/**
	 * @return the current graphic context of this canvas
	 */
	S getNativeGraphicContext();

	/**
	 * Apply a translate transformation to the current transform of the canvas
	 * 
	 * @param x
	 *            displacement along the x axis
	 * @param y
	 *            displacement along the y axis
	 */
	void translate(int x, int y);

	void scale(float scaleX, float scaleY);

	/**
	 * Rotates the current transformation matrix
	 * 
	 * @param angle
	 *            angle in radians
	 */
	void rotate(float angle);

}
