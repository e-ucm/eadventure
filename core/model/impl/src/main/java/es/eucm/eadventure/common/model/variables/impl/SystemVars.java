package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.variables.EAdVarDef;

public class SystemVars {

	public static final EAdVarDef<Integer> MOUSE_X = new EAdVarDefImpl<Integer>(
			"mouse_x", Integer.class, 0);
	
	public static final EAdVarDef<Integer> MOUSE_Y = new EAdVarDefImpl<Integer>(
			"mouse_y", Integer.class, 0);

}
