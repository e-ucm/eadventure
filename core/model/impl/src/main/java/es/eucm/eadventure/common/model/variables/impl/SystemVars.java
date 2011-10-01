package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;

public class SystemVars {

	public static final EAdVarDef<Integer> MOUSE_X = new EAdVarDefImpl<Integer>(
			"mouse_x", Integer.class, 0);

	public static final EAdVarDef<Integer> MOUSE_Y = new EAdVarDefImpl<Integer>(
			"mouse_y", Integer.class, 0);

	/**
	 * Variable containing the active element in the game
	 */
	public static final EAdVarDef<EAdSceneElement> ACTIVE_ELEMENT = new EAdVarDefImpl<EAdSceneElement>(
			"active_element", EAdSceneElement.class, null);

}
