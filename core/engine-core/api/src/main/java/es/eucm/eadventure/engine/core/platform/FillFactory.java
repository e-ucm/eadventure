package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.params.EAdFill;

public interface FillFactory<GraphicContext, Shape> {

	void fill( EAdFill fill, GraphicContext graphicContext, Shape shape );
	
	void fill( EAdFill fill, GraphicContext graphicContext, String text );
	
}
