package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.variables.EAdField;

public abstract class AbstractSceneElementEffect extends AbstractEAdEffect
		implements EAdSceneElementEffect {

	@Param("element")
	private EAdElement element;

	public AbstractSceneElementEffect() {
		super();
	}

	@Override
	public EAdElement getSceneElement() {
		return element;
	}

	public void setSceneElement(EAdSceneElementDef element) {
		this.element = element;
	}

	public void setSceneElement(EAdField<EAdSceneElementDef> elementField) {
		this.element = elementField;
	}

}
