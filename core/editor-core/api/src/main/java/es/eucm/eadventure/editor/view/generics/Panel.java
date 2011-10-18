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
	 * Available layout policies for the panel
	 */
	static enum LayoutPolicy { 
		/**
		 * A policy where each element is placed following the next, minimizing the size of the panel
		 */
		FLOW, 
		/**
		 * A policy where elements are placed next to each other, even if of different sizes
		 */
		HORIZONTAL, 
		/**
		 * A policy where elements are placed on top of each other, even if of different sizes
		 */
		VERTICAL, 
		/**
		 * A policy where elements are stacked on top of each other, each with the same height
		 */
		STRICT_VERTICAL }
	
	/**
	 * @return The list of interface elements in the panel
	 */
	List<InterfaceElement> getElements();

	/**
	 * @return The title of the panel (can be null)
	 */
	String getTitle();
	
	/**
	 * @param element The element to be added to the panel
	 */
	void addElement(InterfaceElement element);
	
	/**
	 * @return the layout policy for this panel
	 */
	LayoutPolicy getLayoutPolicy();

}
