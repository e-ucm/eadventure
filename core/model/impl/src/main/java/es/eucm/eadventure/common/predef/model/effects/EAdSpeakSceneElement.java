package es.eucm.eadventure.common.predef.model.effects;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;

public class EAdSpeakSceneElement extends EAdSpeakEffect {

	private EAdElement element;

	public EAdSpeakSceneElement(EAdElement element) {
		super();
		setId("speakSceneElement");
		this.element = element;
		setOrigin();
	}

	public EAdSpeakSceneElement(EAdSceneElement sceneElement) {
		this((EAdElement) sceneElement);
	}

	private void setOrigin() {
		EAdFieldImpl<Integer> centerX = new EAdFieldImpl<Integer>(element,
				EAdBasicSceneElement.VAR_CENTER_X);
		EAdFieldImpl<Integer> centerY = new EAdFieldImpl<Integer>(element,
				EAdBasicSceneElement.VAR_CENTER_Y);
		
		setPosition(centerX, centerY);

	}

}
