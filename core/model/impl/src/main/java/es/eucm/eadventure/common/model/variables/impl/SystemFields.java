package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

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
	public static final EAdField<EAdSceneElementDef> ACTIVE_ELEMENT = new EAdFieldImpl<EAdSceneElementDef>(
			null, new EAdVarDefImpl<EAdSceneElementDef>("active_element",
					EAdSceneElementDef.class, null));

	public static final Image DEFAULT_MOUSE = new ImageImpl(
			"@drawable/default_cursor.png");

	public static final EAdField<Image> MOUSE_CURSOR = new EAdFieldImpl<Image>(
			null, new EAdVarDefImpl<Image>("mouse_cursor", Image.class,
					DEFAULT_MOUSE));

	public static final EAdField<Boolean> SHOW_MOUSE = new EAdFieldImpl<Boolean>(
			null, new EAdVarDefImpl<Boolean>("show_mouse", Boolean.class, true));

}
