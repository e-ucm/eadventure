package es.eucm.ead.legacyplugins.model;

import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.model.params.variables.VarDef;

public interface LegacyVars {

	public static final EAdVarDef<Boolean> FIRST_PERSON = new VarDef<Boolean>(
			"legacy_first_person", Boolean.class, false);

	public static final EAdVarDef<Integer> SCENE_WIDTH = new VarDef<Integer>(
			"legacy_scene_width", Integer.class, 800);
}
