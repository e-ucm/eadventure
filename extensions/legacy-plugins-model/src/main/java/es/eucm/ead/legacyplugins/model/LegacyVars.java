package es.eucm.ead.legacyplugins.model;

public interface LegacyVars {

	public static final String VAR_PREFIX = "legacy.";

	public static final String FIRST_PERSON = VAR_PREFIX + "first_person";

	public static final String SCENE_WIDTH = VAR_PREFIX + "scene_width";

	// Bubble Event

	/**
	 * EAdString with the name for the bubble
	 */
	public static final String BUBBLE_NAME = VAR_PREFIX + "hud_bubble_name";

	/**
	 * EAdList of operations for the bubble name
	 */
	public static final String BUBBLE_OPERATIONS = VAR_PREFIX
			+ "hud_bubble_operations";
}
