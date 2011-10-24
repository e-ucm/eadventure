package es.eucm.eadventure.common.model.weev.adaptation.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.adaptation.AdaptationProfile;
import es.eucm.eadventure.common.model.weev.impl.AbstractWEEVElement;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default {@link AdaptationProfile} implementation
 */
@Element(detailed = AdaptationProfileImpl.class, runtime = AdaptationProfileImpl.class)
public class AdaptationProfileImpl extends AbstractWEEVElement implements
		AdaptationProfile {

	@Param(value = "name")
	private EAdString name;
	
	public AdaptationProfileImpl() {
		name = EAdString.newEAdString("name");
	}
	
	@Override
	public EAdString getName() {
		return name;
	}

}
