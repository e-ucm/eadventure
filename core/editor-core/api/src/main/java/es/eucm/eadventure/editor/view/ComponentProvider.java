package es.eucm.eadventure.editor.view;

import es.eucm.eadventure.editor.view.generics.InterfaceElement;

/**
 * Provider of interface component.
 * <p>
 * Classes that implement this interface provide a component (of a specific
 * platform, such as a JComponent for Java Swing), for a given interface element
 * that extends {@link InterfaceElement}.
 * 
 * @param <ElementType>
 *            The type of the {@link InterfaceElement}
 * @param <ComponentType>
 *            The type of the actual element in the interface (platform
 *            dependant)
 */
public interface ComponentProvider<ElementType extends InterfaceElement, ComponentType> {

	/**
	 * @param element
	 *            The {@link InterfaceElement} of a specific type
	 * @return The component in the current platform
	 */
	ComponentType getComponent(ElementType element);

}
