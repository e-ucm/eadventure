package ead.engine.core.tracking;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.guievents.DragGEv;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.DragInputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import es.eucm.glas.model.games.traces.HighLevelTrace;
import es.eucm.glas.model.games.traces.LowLevelTrace;
import es.eucm.glas.model.games.traces.LowLevelTrace.Action;
import es.eucm.glas.model.games.traces.LowLevelTrace.Device;
import es.eucm.glas.tracker.GLASTracker;

@Singleton
public class GLASGameTracker extends AbstractGameTracker {

	private GLASTracker tracker;

	private long initTimeStamp;

	@Inject
	public GLASGameTracker(GLASTracker tracker) {
		this.tracker = tracker;
	}

	@Override
	protected void startTrackingImpl(EAdAdventureModel model) {
		String serverURL = model.getProperties().get(SERVER_URL);
		String gameKey = model.getProperties().get(GAME_KEY);
		initTimeStamp = System.currentTimeMillis();
		tracker.startTracking(serverURL, gameKey);
	}

	@Override
	protected void trackImpl(InputAction<?> action, DrawableGO<?> target) {
		LowLevelTrace t = convertToTrace(action, target);
		tracker.track(t);
	}

	private LowLevelTrace convertToTrace(InputAction<?> action,
			DrawableGO<?> target) {
		LowLevelTrace trace = new LowLevelTrace();
		trace.setTarget(((EAdElement) target.getElement()).getId());
		trace.setTimeStamp(System.currentTimeMillis() - initTimeStamp);

		if (action instanceof MouseInputAction) {
			MouseInputAction mouseAction = (MouseInputAction) action;
			trace.setDevice(Device.MOUSE.ordinal());
			LowLevelTrace.Action a = LowLevelTrace.Action.MOVED;
			switch (mouseAction.getType()) {
			case PRESSED:
				a = Action.PRESSED;
				break;
			case RELEASED:
				a = Action.RELEASED;
				break;
			case CLICK:
				a = Action.CLICKED;
				break;
			case DOUBLE_CLICK:
				a = Action.CLICKED;
				break;
			case ENTERED:
				a = Action.ENTERED;
				break;
			case EXITED:
				a = Action.EXITED;
				break;
			case MOVED:
				a = Action.MOVED;
				break;
			case START_DRAG:
				a = Action.START_DRAG;
				break;
			case DRAG:
				a = Action.DRAGGED;
				break;
			case DROP:
				a = Action.DROPPED;
				break;
			case SWIPE_RIGHT:
				a = Action.DRAGGED;
				break;
			case SWIPE_LEFT:
				a = Action.DRAGGED;
				break;

			}

			trace.setAction(a.ordinal());

			int type = 0;
			switch (mouseAction.getButton()) {
			case BUTTON_1:
				type = 1;
				break;
			case BUTTON_2:
				type = 2;
				break;
			case BUTTON_3:
				type = 3;
				break;
			case NO_BUTTON:
				type = 0;
				break;
			}

			trace.setType(type);
			trace.setValue1(mouseAction.getVirtualX());
			trace.setValue2(mouseAction.getVirtualY());

		} else if (action instanceof DragInputAction) {
			DragInputAction dragAction = (DragInputAction) action;
			DragGEv ev = dragAction.getGUIEvent();
			Action a = null;
			switch (ev.getAction()) {
			case ENTERED:
				a = Action.DRAG_ENTERED;
				break;
			case EXITED:
				a = Action.DRAG_EXITED;
				break;
			case DROP:
				a = Action.DROPPED_OVER;
				break;
			}
			trace.setDevice(Device.MOUSE.ordinal());
			trace.setAction(a.ordinal());
			trace.setValue1(dragAction.getVirtualX());
			trace.setValue2(dragAction.getVirtualY());
			trace.setExtra(ev.getCarryElement().getId());
		} else if (action instanceof KeyInputAction) {
			KeyInputAction keyAction = (KeyInputAction) action;
			trace.setDevice(Device.KEYBOARD.ordinal());
			Action a = null;
			switch (keyAction.getType()) {
			case KEY_PRESSED:
				a = Action.PRESSED;
				break;
			case KEY_RELEASED:
				a = Action.RELEASED;
				break;
			case KEY_TYPED:
				a = Action.CLICKED;
				break;
			default:
				a = Action.PRESSED;
			}

			trace.setAction(a.ordinal());
			trace.setType(keyAction.getKeyCode().ordinal());
			trace.setValue1(keyAction.getGUIEvent().getCharacter().charValue());
		}
		return trace;
	}

	@Override
	protected void trackImpl(EffectGO<?> effect) {
		HighLevelTrace t = convertToTrace(effect);
		tracker.track(t);
	}

	private HighLevelTrace convertToTrace(EffectGO<?> effect) {
		HighLevelTrace trace = new HighLevelTrace();
		trace.setTimeStamp(System.currentTimeMillis() - initTimeStamp);
		trace.setType(effect.getEffect().getClass().getName());
		// FIXME more data
		return trace;
	}

}
