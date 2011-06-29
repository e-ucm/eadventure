package es.eucm.eadventure.common.resources.assets.drawable.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;

public class BezierShape implements Shape {
	
	@Param("color")
	private EAdBorderedColor color;
	
	@Param("borderWidth")
	private int borderWidth;
	
	private EAdList<EAdPosition> segments;
	
	private EAdList<Integer> segmentsLength;
	
	public BezierShape( ){
		//segments = new 
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
	

}
