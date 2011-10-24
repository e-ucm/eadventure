package es.eucm.eadventure.common.model.weev.adaptation.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.adaptation.AdaptationProfile;
import es.eucm.eadventure.common.model.weev.adaptation.AdaptationStructure;
import es.eucm.eadventure.common.model.weev.impl.AbstractWEEVElement;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default {@link AdaptationStructure} implementation
 */
@Element(detailed = AdaptationStructureImpl.class, runtime = AdaptationStructureImpl.class)
public class AdaptationStructureImpl extends AbstractWEEVElement implements
		AdaptationStructure {
	
	private EAdList<AdaptationProfile> profiles;
	
	@Param(value = "name")
	private EAdString name;
	
	public AdaptationStructureImpl() {
		profiles = new EAdListImpl<AdaptationProfile>(AdaptationProfile.class);
		name = EAdString.newEAdString("name");
	}

	@Override
	public EAdList<AdaptationProfile> getProfiles() {
		return profiles;
	}

	@Override
	public EAdString getName() {
		return name;
	}

}
