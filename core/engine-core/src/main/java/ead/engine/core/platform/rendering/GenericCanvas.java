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

package ead.engine.core.platform.rendering;

import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.resources.assets.drawable.filters.EAdDrawableFilter;
import ead.common.resources.assets.text.EAdFont;
import ead.common.util.EAdMatrix;
import ead.common.util.EAdRectangle;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.util.EAdTransformation;

/**
 * <p>
 * Parametric canvas, which must be implemented for the different graphic
 * contexts (e.g. Graphics2D in Java)
 * </p>
 * 
 * @param <S>
 */
public interface GenericCanvas<S> {

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
	 * Sets a transformation matrix for the canvas
	 * @param m the matrix
	 */
	void setMatrix(EAdMatrix m);

	/**
	 * @param shape
	 *            Draw a shape in the graphic context (transformations
	 *            configured through
	 *            {@code setTransformation(EAdTransformation t)} are applied)
	 */
	void drawShape(RuntimeDrawable<? extends EAdShape, S> shape);

	/**
	 * Draws the text in 0, 0. Same result can be accomplished calling
	 * {@link GenericCanvas#drawText(String, int, int)} with (0, 0)
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
	// TODO clip with shapes to
	void setClip(EAdRectangle rectangle);

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

	/**
	 * Fills a rectangle with the current paint
	 * 
	 * @param x
	 *            left coordinate
	 * @param y
	 *            top coordiante
	 * @param width
	 *            width for the rectangle
	 * @param height
	 *            height for the rectangle
	 */
	void fillRect(int x, int y, int width, int height);

	/**
	 * Sets the filter to be used in the rendering
	 * 
	 * @param filter
	 *            the filter. {@code null} if no filter must be applied
	 */
	void setFilter(RuntimeDrawable<?, S> drawable, EAdDrawableFilter filter);

}
