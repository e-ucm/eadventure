package es.eucm.eadventure.common.model.effects.impl.enums;

import com.gwtent.reflection.client.Reflectable;

@Reflectable
public enum InterpolationType {

	/**
	 * Linear interpolation
	 */
	LINEAR,

	/**
	 * Bounces in the end
	 */
	BOUNCE_END, ACCELERATE, DESACCELERATE;
}
