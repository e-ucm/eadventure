package es.eucm.eadventure.engine.core.platform.assets.impl;

import java.awt.geom.GeneralPath;

import es.eucm.eadventure.common.model.params.EAdPosition;

public class DesktopBezierShape extends RuntimeBezierShape {
	
	private GeneralPath path;

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		path = new GeneralPath();
		
		EAdPosition p = shape.getPoints().get(0);
		path.moveTo(p.getX(), p.getY());
		
		int pointIndex = 1;
		EAdPosition p1, p2, p3;
		for ( Integer i: shape.getSegmentsLength() ){
				switch( i ){
				case 1:
					p1 = shape.getPoints().get(pointIndex++);
					path.lineTo(p1.getX(), p1.getY());
					break;
				case 2:
					p1 = shape.getPoints().get(pointIndex++);
					p2 = shape.getPoints().get(pointIndex++);
					path.quadTo(p1.getX(), p1.getY(), p2.getX(), p2.getY());
					break;
				case 3:
					p1 = shape.getPoints().get(pointIndex++);
					p2 = shape.getPoints().get(pointIndex++);
					p3 = shape.getPoints().get(pointIndex++);
					path.curveTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
					break;			
			}
		}
		
		if ( shape.isClosed() )
			path.closePath();
		
		return true;
	}
	
	public GeneralPath getShape( ){
		return path;
	}

}
