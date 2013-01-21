package ead.common.model.elements.effects;

import ead.common.interfaces.Param;
import ead.common.model.elements.scenes.EAdSceneElement;

/**
 * Removes an scene element from the scene
 *
 */
public class RemoveEf extends AbstractEffect {

	@Param("element")
	private EAdSceneElement element;

	public RemoveEf() {

	}

	public EAdSceneElement getElement() {
		return element;
	}

	public void setElement(EAdSceneElement element) {
		this.element = element;
	}

}
