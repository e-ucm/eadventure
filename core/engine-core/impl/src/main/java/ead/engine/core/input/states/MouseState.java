package ead.engine.core.input.states;

import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.engine.core.input.InputState;

public class MouseState implements InputState {

	public static final MouseState POINTER_INSIDE = new MouseState(
			MouseGEvButtonType.NO_BUTTON, false, true);

	public static final MouseState LEFT_BUTTON_PRESSED = new MouseState(
			MouseGEvButtonType.BUTTON_1, true, true);
	public static final MouseState RIGHT_BUTTON_PRESSED = new MouseState(
			MouseGEvButtonType.BUTTON_3, true, true);

	private MouseGEvButtonType mouseButton;

	private boolean pressed;

	private boolean isInside;

	private MouseState(MouseGEvButtonType mouseButton, boolean pressed,
			boolean isInside) {
		this.mouseButton = mouseButton;
		this.pressed = pressed;
		this.isInside = isInside;
	}

	public MouseGEvButtonType getMouseButton() {
		return mouseButton;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isInside() {
		return isInside;
	}

}
