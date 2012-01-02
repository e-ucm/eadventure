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

package ead.common.resources.assets.drawable.basics.shapes;

import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.params.fills.EAdPaintImpl;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.Shape;
import ead.common.util.EAdPosition;

public class BezierShape implements Shape, Cloneable {

	@Param("paint")
	private EAdPaint paint;

	@Param("closed")
	private boolean closed;

	@Param("points")
	private EAdList<EAdPosition> points;

	@Param("segmentsLength")
	private EAdList<Integer> segmentsLength;

	public BezierShape() {
		points = new EAdListImpl<EAdPosition>(EAdPosition.class);
		segmentsLength = new EAdListImpl<Integer>(Integer.class);
		paint = EAdPaintImpl.TRANSPARENT;
	}
	
	public BezierShape( EAdPaint paint ){
		this();
		this.paint = paint;
	}

	public BezierShape(EAdPosition startPoint) {
		this();
		init(startPoint);

	}

	private void init(EAdPosition startPoint) {
		points.add(startPoint);
		closed = false;

	}

	public BezierShape(int x, int y) {
		this(new EAdPosition(x, y));
	}

	/**
	 * Resets the shape and sets the initial point to x and y
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void moveTo(int x, int y) {
		init(new EAdPosition(x, y));
	}

	/**
	 * <p>
	 * Return the color with which to draw the shape.
	 * </p>
	 * <p>
	 * The color can have alpha = 0 if the shape is expected to be invisible. As
	 * it uses {@link EAdBorderedColor} it can be used to paint just the outline
	 * of the shape.
	 * </p>
	 * 
	 * @return The color of the shape
	 */
	public EAdPaint getPaint() {
		return paint;
	}

	/**
	 * Sets the paint for the shape
	 * 
	 * @param paint
	 *            the paint
	 */
	public void setPaint(EAdPaint paint) {
		this.paint = paint;
	}

	public void lineTo(EAdPosition p) {
		points.add(p);
		segmentsLength.add(1);
	}

	public void lineTo(int x, int y) {
		this.lineTo(new EAdPosition(x, y));
	}

	public void quadTo(EAdPosition p1, EAdPosition p2) {
		points.add(p1);
		points.add(p2);
		segmentsLength.add(2);
	}

	public void curveTo(EAdPosition p1, EAdPosition p2, EAdPosition p3) {
		points.add(p1);
		points.add(p2);
		points.add(p3);
		segmentsLength.add(3);
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isClosed() {
		return closed;
	}

	public EAdList<EAdPosition> getPoints() {
		return points;
	}

	public EAdList<Integer> getSegmentsLength() {
		return segmentsLength;
	}

	public void cubeTo(int x1, int y1, int x2, int y2) {
		quadTo(new EAdPosition(x1, y1), new EAdPosition(x2, y2));
	}
	
	public Object clone(){
		BezierShape s = new BezierShape();
		s.closed = closed;
		for ( Integer i: segmentsLength ){
			s.segmentsLength.add(new Integer(i.intValue()));
		}
		
		for ( EAdPosition p: points ){
			s.points.add(new EAdPosition( p.getX(), p.getY()));
		}
		
		s.paint = paint;
		
		return s;
	}
	
	
	

}
