package es.eucm.eadventure.editor.view.generics;

/**
 * An option in the user interface.
 * <p>
 * This type of interface element allows for the display and modification of the
 * value of a field though the use of a {@link FieldDescriptor}. Optionally a
 * title and tooltiptext can be defined for the element.
 * 
 * @param <S>
 */
public interface Option<S> extends InterfaceElement {

	/**
	 * @return the title to be used in the interface (can be null)
	 */
	String getTitle();

	/**
	 * @return the tooltiptext for the interface, to help users (should not be
	 *         left null)
	 */
	String getToolTipText();

	/**
	 * @return the {@link FieldDescriptor} for the field that is displayed and
	 *         modified by this option element
	 */
	FieldDescriptor<S> getFieldDescriptor();

}
