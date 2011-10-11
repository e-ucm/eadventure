package es.eucm.eadventure.editor.view.generics;

import java.util.List;

/**
 * A panel interface element.
 * <p>
 * This element type allows for the display of several elements grouped in the
 * interface
 */
public interface Panel extends InterfaceElement {

	/**
	 * @return The list of interface elements in the panel
	 */
	List<InterfaceElement> getElements();

	/**
	 * @return The title of the panel (can be null)
	 */
	String getTitle();

}
