package es.eucm.eadventure.common.interfaces.features;

import es.eucm.eadventure.common.params.EAdString;

public interface Documented {

	/**
	 * @return the {@link EAdString} of the documentation
	 */
	EAdString getDoc();
	
	/**
	 * 
	 * @return the {@link EAdString} of the short description for the element
	 */
	EAdString getDesc();
	
	/**
	 * 
	 * @return the {@link EAdString} of the long description for the element
	 */
	EAdString getDetailDesc();	

}
