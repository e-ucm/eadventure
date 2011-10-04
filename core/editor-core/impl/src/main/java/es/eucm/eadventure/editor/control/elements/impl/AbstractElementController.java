package es.eucm.eadventure.editor.control.elements.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.editor.control.ElementController;
import es.eucm.eadventure.editor.view.generics.Panel;

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

	@Override
	public Panel getPanel(View view) {
		// TODO Auto-generated method stub
		return null;
	}

}
