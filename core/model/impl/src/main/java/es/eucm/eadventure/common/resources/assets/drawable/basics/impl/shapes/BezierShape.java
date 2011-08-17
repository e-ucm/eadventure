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

package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Shape;

public class BezierShape implements Shape, Cloneable {

	@Param("fill")
	private EAdFill fill;

	@Param("closed")
	private boolean closed;

	private EAdList<EAdPositionImpl> points;

	private EAdList<Integer> segmentsLength;

	public BezierShape() {
		points = new EAdListImpl<EAdPositionImpl>(EAdPositionImpl.class);
		segmentsLength = new EAdListImpl<Integer>(Integer.class);
		fill = EAdBorderedColor.TRANSPARENT;
	}

	public BezierShape(EAdPositionImpl startPoint) {
		this();
		init(startPoint);

	}

	private void init(EAdPositionImpl startPoint) {
		points.add(startPoint);
		closed = false;

	}

	public BezierShape(int x, int y) {
		this(new EAdPositionImpl(x, y));
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
		init(new EAdPositionImpl(x, y));
	}

	@Override
	public EAdFill getFill() {
		return fill;
	}

	public void setFill(EAdFill color) {
		this.fill = (EAdFill) color;
	}

	public void lineTo(EAdPositionImpl p) {
		points.add(p);
		segmentsLength.add(1);
	}

	public void lineTo(int x, int y) {
		this.lineTo(new EAdPositionImpl(x, y));
	}

	public void quadTo(EAdPositionImpl p1, EAdPositionImpl p2) {
		points.add(p1);
		points.add(p2);
		segmentsLength.add(2);
	}

	public void curveTo(EAdPositionImpl p1, EAdPositionImpl p2, EAdPositionImpl p3) {
		points.add(p1);
		points.add(p2);
		points.add(p3);
		segmentsLength.add(3);
	}

	public void close() {
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	public EAdList<EAdPositionImpl> getPoints() {
		return points;
	}

	public EAdList<Integer> getSegmentsLength() {
		return segmentsLength;
	}

	public void cubeTo(int x1, int y1, int x2, int y2) {
		quadTo(new EAdPositionImpl(x1, y1), new EAdPositionImpl(x2, y2));
	}
	
	public Object clone(){
		BezierShape s = new BezierShape();
		s.closed = closed;
		for ( Integer i: segmentsLength ){
			s.segmentsLength.add(new Integer(i.intValue()));
		}
		
		for ( EAdPositionImpl p: points ){
			s.points.add(new EAdPositionImpl( p.getX(), p.getY()));
		}
		
		s.fill = fill;
		
		return s;
	}

}
