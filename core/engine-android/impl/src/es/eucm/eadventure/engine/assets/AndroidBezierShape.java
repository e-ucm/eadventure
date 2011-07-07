package es.eucm.eadventure.engine.assets;

import android.graphics.Path;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeBezierShape;

public class AndroidBezierShape extends RuntimeBezierShape {
	
	private Path path;

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		path = new Path();
		
		EAdPosition p = descriptor.getPoints().get(0);
		path.moveTo(p.getX(), p.getY());
		
		int pointIndex = 1;
		EAdPosition p1, p2, p3;
		for ( Integer i: descriptor.getSegmentsLength() ){
				switch( i ){
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
					//TODO no curveTO! addArc?
					//path.curveTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
					break;			
			}
		}
		
		if ( descriptor.isClosed() )
			path.close();
		
		return true;
	}
	
	public Path getShape( ){
		return path;
	}

}
