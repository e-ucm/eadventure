package ead.common.resources.assets.drawable.basics.shapes;

import ead.common.interfaces.Param;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.EAdShape;

public class AbstractShape implements EAdShape {
	
	@Param("paint")
	private EAdPaint paint;
	
	public AbstractShape( EAdPaint paint ){
		this.paint = paint;
	}
	
	public AbstractShape( ){
		
	}

	@Override
	public EAdPaint getPaint() {
		return paint;
	}

	@Override
	public void setPaint(EAdPaint paint) {
		this.paint = paint;		
	}

}
