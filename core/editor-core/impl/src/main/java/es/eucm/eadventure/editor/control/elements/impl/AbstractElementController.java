package es.eucm.eadventure.editor.control.elements.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.editor.control.ElementController;

public abstract class AbstractElementController<S extends EAdElement> implements ElementController<S> {

	S element;
	
	@Override
	public void setElement(S element) {
		this.element = element;
	}

	@Override
	public S getElement() {
		return element;
	}

}
