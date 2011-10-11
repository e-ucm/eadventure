package es.eucm.eadventure.editor.control;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.editor.view.generics.Panel;

/**
 * Controller for a specific element in the model
 * 
 * @param <S>
 *            A type of element in the model, which must inherit from
 *            {@link EAdElement}
 */
public interface ElementController<S extends EAdElement> {

	/**
	 * The types of views in the editor
	 */
	public static enum View {
		EXPERT, ADVANCED, SIMPLE
	};

	/**
	 * @param element
	 *            The element controlled by this controller
	 */
	void setElement(S element);

	/**
	 * @return The element controlled by this controller
	 */
	S getElement();

	/**
	 * @param view
	 *            The current view in the editor
	 * @return The panel for the corresponding {@link View}
	 */
	Panel getPanel(View view);

}
