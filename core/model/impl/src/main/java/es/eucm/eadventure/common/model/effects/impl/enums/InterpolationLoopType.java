package es.eucm.eadventure.common.model.effects.impl.enums;

import com.gwtent.reflection.client.Reflectable;

@Reflectable
public enum InterpolationLoopType {
	/**
	 * No loop
	 */
	NO_LOOP,

	/**
	 * When interpolations ends, goes backwards
	 */
	REVERSE,

	/**
	 * When interpolation ends, it restarts
	 */
	RESTART
}
