package es.eucm.eadventure.engine.core.platform.impl;

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.FontHandler;

public abstract class AbstractCanvas<T> implements EAdCanvas<T> {
	
	protected static final Logger logger = Logger.getLogger("EAdCanvas");
	
	protected T g;
	
	protected EAdPaint paint;
	
	protected FontHandler fontHandler;
	
	@Inject
	public AbstractCanvas( FontHandler fontHandler ){
		this.fontHandler = fontHandler;
	}
	
	public void setGraphicContext( T g ){
		this.g = g;
	}
	
	public void drawText( String text ){
		drawText( text, 0, 0 );
	}
	
	public void setPaint( EAdPaint paint ){
		this.paint = paint;
	}
	
	@Override
	public T getNativeGraphicContext() {
		return g;
	}

}
