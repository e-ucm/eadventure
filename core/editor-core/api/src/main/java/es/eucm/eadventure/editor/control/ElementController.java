package es.eucm.eadventure.editor.control;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.editor.view.generics.Panel;

public interface ElementController<S extends EAdElement> {

	public static enum View { EXPERT, ADVANCED, SIMPLE };
	
	void setElement(S element);
	
	S getElement();
	
	Panel getPanel(View view);
	
}
