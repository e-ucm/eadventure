package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;

@Element(runtime = EAdMakeActiveElementEffect.class, detailed = EAdMakeActiveElementEffect.class)
public class EAdMakeActiveElementEffect extends AbstractEAdEffect implements EAdSceneElementEffect {
	
	@Param("sceneElement")
	protected EAdSceneElement sceneElement;
	
	public EAdMakeActiveElementEffect(String id) {
		super(id);
	}
	
	public void setSceneElement( EAdSceneElement sceneElement ){
		this.sceneElement = sceneElement;
	}
	
	public EAdSceneElement getSceneElement( ){
		return sceneElement;
	}

}
