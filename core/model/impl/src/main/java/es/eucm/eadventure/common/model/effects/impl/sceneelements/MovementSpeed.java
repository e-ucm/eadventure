package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import com.gwtent.reflection.client.Reflectable;

/**
 * 
 * Enum with all possibles speeds for the movement effect
 * 
 */
@Reflectable
public enum MovementSpeed {

	SLOW, NORMAL, FAST, INSTANT, CUSTOM;

	public float getSpeedFactor() {
		switch (this) {
		case FAST:
			return 0.5f;
		case SLOW:
			return 2.0f;
		case INSTANT:
			return 0.0f;
		default:
			return 1.0f;
		}
	}
}
