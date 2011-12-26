package es.eucm.eadventure.engine.core.input.states;

import es.eucm.eadventure.common.model.elements.guievents.enums.MouseButtonType;
import es.eucm.eadventure.engine.core.input.InputState;

public class MouseState implements InputState {

	public static final MouseState POINTER_INSIDE = new MouseState(
			MouseButtonType.NO_BUTTON, false, true);

	public static final MouseState LEFT_BUTTON_PRESSED = new MouseState(
			MouseButtonType.BUTTON_1, true, true);
	public static final MouseState RIGHT_BUTTON_PRESSED = new MouseState(
			MouseButtonType.BUTTON_3, true, true);

	private MouseButtonType mouseButton;

	private boolean pressed;

	private boolean isInside;

	private MouseState(MouseButtonType mouseButton, boolean pressed,
			boolean isInside) {
		this.mouseButton = mouseButton;
		this.pressed = pressed;
		this.isInside = isInside;
	}

	public MouseButtonType getMouseButton() {
		return mouseButton;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isInside() {
		return isInside;
	}

}
