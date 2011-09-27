package es.eucm.eadventure.engine.core.util.impl;

import es.eucm.eadventure.engine.core.util.EAdMatrix;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class EAdTransformationImpl implements EAdTransformation {
	
	private EAdMatrix matrix;
	
	private boolean visible;
	
	private float alpha;
	
	public EAdTransformationImpl( EAdMatrix matrix, boolean visible, float alpha ){
		this.matrix = matrix;
		this.visible = visible;
		this.alpha = alpha;
	}
	
	public EAdTransformationImpl(){
		matrix = new EAdMatrixImpl();
		visible = true;
	}

	@Override
	public EAdMatrix getMatrix() {
		return matrix;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

}
