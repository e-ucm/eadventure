package es.eucm.eadventure.common.params.paint;

import es.eucm.eadventure.common.params.EAdParam;

public interface EAdPaint extends EAdParam {
	
	EAdFill getBorder();
	
	EAdFill getFill();
	
	int getBorderWidth();

}
