package es.eucm.eadventure.engine.core.util;

public interface EAdTransformation extends Cloneable {
	
	EAdMatrix getMatrix();
	
	boolean isVisible();
	
	float getAlpha();
	
	Object clone();

}
