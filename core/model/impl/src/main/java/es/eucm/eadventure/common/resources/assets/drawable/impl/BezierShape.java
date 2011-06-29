package es.eucm.eadventure.common.resources.assets.drawable.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;

public class BezierShape implements Shape {

	@Param("color")
	private EAdBorderedColor color;
	
	@Param("borderWidth")
	private int borderWidth;
	
	@Param("closed")
	private boolean closed;
	
	private EAdList<EAdPosition> points;
	
	private EAdList<Integer> segmentsLength;
	
	public BezierShape( EAdPosition startPoint ){
		points = new EAdListImpl<EAdPosition>(EAdPosition.class);
		segmentsLength = new EAdListImpl<Integer>(Integer.class);
		points.add(startPoint);
		closed = false;
		color = EAdBorderedColor.TRANSPARENT;
	}
	
	public BezierShape(int x, int y) {
		this( new EAdPosition( x, y ));
	}

	@Override
	public EAdBorderedColor getColor() {
		return color;
	}
	
	@Override
	public int getBorderWidth() {
		return borderWidth;
	}

	
	public void setColor(EAdBorderedColor color) {
		this.color = color;
	}
	
	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
	
	public void lineTo( EAdPosition p ){
		points.add(p);
		segmentsLength.add(1);
	}
	
	public void lineTo(int x, int y ) {
		this.lineTo(new EAdPosition(x, y ));
	}
	
	public void cubeTo( EAdPosition p1, EAdPosition p2 ){
		points.add(p1);
		points.add(p2);
		segmentsLength.add(2);
	}
	
	public void quadTo( EAdPosition p1, EAdPosition p2, EAdPosition p3 ){
		points.add(p1);
		points.add(p2);
		points.add(p3);
		segmentsLength.add(3);
	}
	
	public void close( ){
		closed = true;
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

}
