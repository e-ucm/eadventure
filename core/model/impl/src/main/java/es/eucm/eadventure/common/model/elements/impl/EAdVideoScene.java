package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;

@Element(detailed = EAdVideoScene.class, runtime = EAdVideoScene.class)
public class EAdVideoScene extends EAdSceneImpl implements EAdScene {

	@Bundled
	@Asset({ Video.class })
	public static final String video = "video";
	
	@Param("videoFinishedVar")
	private BooleanVar videoFinishedVar;

	public EAdVideoScene(String id) {
		super(id);
		videoFinishedVar = new BooleanVar(id + "_videoFinishedVar");
	}

	public BooleanVar getVideoFinishedVar() {
		return videoFinishedVar;
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
