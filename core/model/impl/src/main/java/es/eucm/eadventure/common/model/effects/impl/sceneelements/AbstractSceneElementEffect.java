package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.EAdField;

public abstract class AbstractSceneElementEffect extends AbstractEAdEffect
		implements EAdSceneElementEffect {

	@Param("element")
	private EAdSceneElement element;

	@Param("elementField")
	private EAdField<EAdSceneElement> elementField;

	public AbstractSceneElementEffect(String id) {
		super(id);
	}

	@Override
	public EAdSceneElement getSceneElement() {
		return element;
	}

	@Override
	public EAdField<EAdSceneElement> getSceneElementField() {
		return elementField;
	}

	public void setSceneElement(EAdSceneElement element) {
		this.element = element;
	}

	public void setSceneElementField(EAdField<EAdSceneElement> elementField) {
		this.elementField = elementField;
	}

}
