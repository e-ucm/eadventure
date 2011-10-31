package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.EAdField;

public abstract class AbstractSceneElementEffect extends AbstractEAdEffect
		implements EAdSceneElementEffect {

	@Param("element")
	private EAdElement element;

	public AbstractSceneElementEffect(String id) {
		super(id);
	}

	@Override
	public EAdElement getSceneElement() {
		return element;
	}

	public void setSceneElement(EAdSceneElement element) {
		this.element = element;
	}

	public void setSceneElement(EAdField<EAdSceneElement> elementField) {
		this.element = elementField;
	}

}
