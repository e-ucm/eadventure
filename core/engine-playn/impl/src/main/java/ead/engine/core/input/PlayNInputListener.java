package ead.engine.core.input;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;

public class PlayNInputListener implements Mouse.Listener, Keyboard.Listener {

	private InputHandler inputHandler;

	public PlayNInputListener(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		inputHandler.addAction(new MouseInputAction(MouseGEv.getMouseEvent(
				MouseGEvType.PRESSED, getMouseButton2(event.button())),
				(int) event.x(), (int) event.y()));
	}

	@Override
	public void onMouseUp(ButtonEvent event) {
		inputHandler.addAction(new MouseInputAction(MouseGEv.getMouseEvent(
				MouseGEvType.RELEASED, getMouseButton2(event.button())),
				(int) event.x(), (int) event.y()));
		inputHandler.addAction(new MouseInputAction(MouseGEv.getMouseEvent(
				MouseGEvType.CLICK, getMouseButton2(event.button())),
				(int) event.x(), (int) event.y()));

	}

	@Override
	public void onMouseMove(MotionEvent event) {
		inputHandler.addAction(new MouseInputAction(MouseGEv.MOUSE_MOVED,
				(int) event.x(), (int) event.y()));

	}

	@Override
	public void onMouseWheelScroll(WheelEvent event) {

	}

	private MouseGEvButtonType getMouseButton2(int b) {
		switch (b) {
		case Mouse.BUTTON_LEFT:
			return MouseGEvButtonType.BUTTON_1;
		case Mouse.BUTTON_MIDDLE:
			return MouseGEvButtonType.BUTTON_2;
		case Mouse.BUTTON_RIGHT:
			return MouseGEvButtonType.BUTTON_3;
		default:
			return MouseGEvButtonType.NO_BUTTON;
		}
	}

	@Override
	public void onKeyDown(Event event) {
		KeyInputAction action = getKeyboardAction(KeyEventType.KEY_PRESSED,
				event.key());
		if (action != null)
			inputHandler.addAction(action);

	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// FIXME probably is a good idea to use only virtual keys
	}

	@Override
	public void onKeyUp(Event event) {
		KeyInputAction action = getKeyboardAction(KeyEventType.KEY_RELEASED,
				event.key());
		if (action != null)
			inputHandler.addAction(action);

	}

	public KeyInputAction getKeyboardAction(KeyEventType actionType, Key key) {
		switch (key) {
		case UP:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_UP);
		case DOWN:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_DOWN);
		case LEFT:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_LEFT);
		case RIGHT:
			return new KeyInputAction(actionType, KeyGEvCode.ARROW_RIGHT);
		case ENTER:
			return new KeyInputAction(actionType, KeyGEvCode.RETURN);
		case ESCAPE:
			return new KeyInputAction(actionType, KeyGEvCode.ESC);
		}
		return null;
	}
}
