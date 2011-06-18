package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;

@Element(detailed = EAdVideoScene.class, runtime = EAdVideoScene.class)
public class EAdVideoScene extends EAdSceneImpl implements EAdScene {

	@Asset({ Video.class })
	public static final String video = "video";
	
	@Param("videoFinishedVar")
	private BooleanVar videoFinishedVar;
	
	private EAdElementList<EAdEffect> finalEffects;

	public EAdVideoScene(String id) {
		super(id);
		videoFinishedVar = new BooleanVar(id + "_videoFinishedVar");
		finalEffects = new EAdElementListImpl<EAdEffect>(EAdEffect.class);
	}
	
	public BooleanVar getVideoFinishedVar() {
		return videoFinishedVar;
	}
	
	@Override
	public EAdElementList<EAdSceneElement> getSceneElements() {
		return null;
	}

	public EAdElementList<EAdEffect> getFinalEffects() {
		return finalEffects;
	}

	@Override
	public boolean isReturnable() {
		return false;
	}	
	
	public void setUpForEngine() {
		finalEffects.add(new EAdChangeVarValueEffect("finishVide", videoFinishedVar, new BooleanOperation("id", EmptyCondition.TRUE_EMPTY_CONDITION)));
		finalEffects.add(new EAdChangeScene("changeScene"));

	}

}
