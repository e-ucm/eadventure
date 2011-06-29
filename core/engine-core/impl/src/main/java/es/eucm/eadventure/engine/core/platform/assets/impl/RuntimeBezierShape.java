package es.eucm.eadventure.engine.core.platform.assets.impl;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;

public abstract class RuntimeBezierShape implements DrawableAsset<BezierShape>{
	
	protected BezierShape shape;
	
	protected boolean loaded = false;
	
	protected int width = 0;
	
	protected int height = 0;
	
	@Override
	public boolean loadAsset(){
		int point = 0;
		
		EAdPosition p = shape.getPoints().get(point);
		int xMax = p.getX();
		int xMin = p.getX();
		int yMax = p.getY();
		int yMin = p.getY(); 
		
		for ( Integer i : shape.getSegmentsLength() ){
			point += i;
			p = shape.getPoints().get(point);
			xMax = xMax < p.getX() ? p.getX() : xMax;
			xMin = xMin > p.getX() ? p.getX() : xMin;
			yMax = yMax < p.getY() ? p.getY() : yMax;
			yMin = yMin > p.getY() ? p.getY() : yMin;
		}
		
		width = xMax - xMin;
		height = yMax - yMin;
		return true;
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void freeMemory() {
		
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public void setDescriptor(BezierShape descriptor) {
		this.shape = descriptor;
		
	}

	@Override
	public void update(GameState state) {
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S> getDrawable() {
		return (DrawableAsset<S>) this;
	}
	
	@Override
	public BezierShape getAssetDescriptor() {
		return shape;
	}
	

}
