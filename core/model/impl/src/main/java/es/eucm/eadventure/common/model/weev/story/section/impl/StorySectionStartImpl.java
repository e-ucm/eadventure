package es.eucm.eadventure.common.model.weev.story.section.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.adaptation.AdaptationProfile;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNode;
import es.eucm.eadventure.common.model.weev.story.section.StorySectionEnd;
import es.eucm.eadventure.common.model.weev.story.section.StorySectionStart;

/**
 * Default implementation of {@link StorySectionEnd}
 */
@Element(detailed = StorySectionStartImpl.class, runtime = StorySectionStartImpl.class)
public class StorySectionStartImpl extends AbstractNode implements
		StorySectionStart {

	@Param(value = "adaptationProfile")
	private AdaptationProfile adaptationProfile;
	
	@Override
	public AdaptationProfile getAdaptationProfile() {
		return adaptationProfile;
	}
	
	public void setAdaptationProfile(AdaptationProfile adaptationProfile) {
		this.adaptationProfile = adaptationProfile;
	}


}
