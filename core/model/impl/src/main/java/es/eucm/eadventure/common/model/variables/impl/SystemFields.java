package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.EAdField;

public class SystemFields {

	public static final EAdField<Integer> MOUSE_X = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("mouse_x", Integer.class, 0));

	public static final EAdField<Integer> MOUSE_Y = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("mouse_y", Integer.class, 0));

	public static final EAdField<Integer> GUI_WIDTH = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("gui_width", Integer.class, 800));

	public static final EAdField<Integer> GUI_HEIGHT = new EAdFieldImpl<Integer>(
			null, new EAdVarDefImpl<Integer>("gui_height", Integer.class, 600));

	/**
	 * Variable containing the active element in the game
	 */
	public static final EAdField<EAdSceneElement> ACTIVE_ELEMENT = new EAdFieldImpl<EAdSceneElement>(
			null, new EAdVarDefImpl<EAdSceneElement>("active_element",
					EAdSceneElement.class, null));

}
