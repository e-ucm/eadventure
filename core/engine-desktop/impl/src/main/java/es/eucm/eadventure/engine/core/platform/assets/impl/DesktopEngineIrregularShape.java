package es.eucm.eadventure.engine.core.platform.assets.impl;

import java.awt.Polygon;

import es.eucm.eadventure.common.model.params.EAdPosition;

public class DesktopEngineIrregularShape extends RuntimeIrregularShape {

	private Polygon polygon;
	
	@Override
	public boolean loadAsset() {
		super.loadAsset();
		polygon = new Polygon();
		for (EAdPosition pos : shape.getPositions())
			polygon.addPoint(pos.getX(), pos.getY());
		
		return true;
	}

	public Polygon getPolygon() {
		return polygon;
	}
	
}
