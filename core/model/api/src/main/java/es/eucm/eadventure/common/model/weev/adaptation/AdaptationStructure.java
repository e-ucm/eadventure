package es.eucm.eadventure.common.model.weev.adaptation;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.weev.WEEVElement;
import es.eucm.eadventure.common.params.EAdString;

public interface AdaptationStructure extends WEEVElement {

	EAdList<AdaptationProfile> getProfiles();
	
	EAdString getName();
	
}
