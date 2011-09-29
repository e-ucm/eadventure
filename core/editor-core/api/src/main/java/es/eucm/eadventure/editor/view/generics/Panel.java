package es.eucm.eadventure.editor.view.generics;

import java.util.List;

public interface Panel extends InterfaceElement {
	
	List<InterfaceElement> getElements();
	
	String getTitle();
	
}
