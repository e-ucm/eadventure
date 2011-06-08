package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;

public class EAdVideoScene extends EAdSceneImpl implements EAdScene {

	@Bundled
	@Asset({ Video.class })
	public static final String video = "video";

	public EAdVideoScene(String id) {
		super(id);
	}

	@Override
	public EAdElementList<EAdSceneElement> getSceneElements() {
		return null;
	}

	@Override
	public boolean isReturnable() {
		return false;
	}	

}
