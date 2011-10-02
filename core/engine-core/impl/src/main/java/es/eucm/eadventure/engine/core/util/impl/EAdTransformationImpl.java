package es.eucm.eadventure.engine.core.util.impl;

import es.eucm.eadventure.engine.core.util.EAdMatrix;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class EAdTransformationImpl implements EAdTransformation {

	public static final EAdTransformation INITIAL_TRANSFORMATION = new EAdTransformationImpl();

	private EAdMatrix matrix;

	private boolean visible;

	private float alpha;

	public EAdTransformationImpl(EAdMatrix matrix, boolean visible, float alpha) {
		this.matrix = matrix;
		this.visible = visible;
		this.alpha = alpha;
	}

	public EAdTransformationImpl() {
		matrix = new EAdMatrixImpl();
		visible = true;
		alpha = 1.0f;
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
	
	public void setAlpha(float alpha){
		this.alpha = alpha;
	}
	
	public void setVisible( boolean visible ){
		this.visible = visible;
	}
	
	public Object clone(){
		EAdTransformationImpl t = new EAdTransformationImpl();
		t.alpha = alpha;
		t.visible = visible;
		t.matrix = new EAdMatrixImpl(matrix.getFlatMatrix());
		return t;
		
	}
	

}
