/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.engine.core.tracking;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.guievents.DragGEv;
import ead.common.model.elements.variables.EAdField;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.effects.EffectGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.DragInputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.tracking.selection.TrackerSelector;
import es.eucm.glas.model.TrackData;
import es.eucm.glas.model.games.traces.ActionTrace;
import es.eucm.glas.model.games.traces.ActionTrace.Action;
import es.eucm.glas.model.games.traces.ActionTrace.Device;
import es.eucm.glas.model.games.traces.LogicTrace;
import es.eucm.glas.tracker.GLASTracker;
import es.eucm.glas.tracker.TrackDataListener;

@Singleton
public class GLASGameTracker extends AbstractGameTracker {

	private GLASTracker tracker;

	private long initTimeStamp;

	private static final int DEFAULT_MAX_TRACES = 200;

	private ValueMap valueMap;

	@Inject
	public GLASGameTracker(ValueMap valueMap, GLASTracker tracker,
			TrackerSelector selector) {
		super(selector);
		this.tracker = tracker;
		this.valueMap = valueMap;
		logger.info("GLAS Game tracker created");
	}

	@Override
	protected void startTrackingImpl(EAdAdventureModel model) {
		String serverURL = model.getProperties().get(SERVER_URL);
		String gameKey = model.getProperties().get(GAME_KEY);
		initTimeStamp = System.currentTimeMillis();
		logger.info("Starting tracking...");
		tracker.startTracking(serverURL, gameKey, new TrackDataListener() {

			@Override
			public void trackDataReceived(TrackData trackData) {
				logger.info("Track data received.");
			}

			@Override
			public void error(String errorMessage) {
				logger.warn(errorMessage);
				logger.warn("Tracking will be disabled.");
				GLASGameTracker.this.stop();

			}

		});
		try {
			String maxTracesProp = model.getProperties().get(MAX_TRACES);
			int maxTraces = maxTracesProp == null ? DEFAULT_MAX_TRACES
					: Integer.parseInt(maxTracesProp);
			tracker.setMaxTraces(maxTraces);
		} catch (NumberFormatException e) {
			tracker.setMaxTraces(DEFAULT_MAX_TRACES);
			logger.info(
					"Invalid number for {} parameter. Set to default value.",
					MAX_TRACES);
		}

	}

	@Override
	protected void trackImpl(InputAction<?> action, SceneElementGO<?> target) {
		ActionTrace t = convertToTrace(action, target);
		tracker.track(t);
	}

	private ActionTrace convertToTrace(InputAction<?> action,
			SceneElementGO<?> target) {
		ActionTrace trace = new ActionTrace();
		if (target.getElement() != null) {
			trace.setTarget(((EAdElement) target.getElement()).getId());
		}
		trace.setTimeStamp(System.currentTimeMillis() - initTimeStamp);

		if (action instanceof MouseInputAction) {
			MouseInputAction mouseAction = (MouseInputAction) action;
			trace.setDevice(Device.MOUSE.ordinal());
			ActionTrace.Action a = ActionTrace.Action.MOVED;
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
			if (keyAction.getCharacter() != null) {
				trace.setValue1(keyAction.getCharacter());
			}
		}
		return trace;
	}

	@Override
	protected void trackImpl(EffectGO<?> effect) {
		LogicTrace t = convertToTrace(effect);
		tracker.track(t);
	}

	private LogicTrace convertToTrace(EffectGO<?> effect) {
		LogicTrace trace = new LogicTrace();
		trace.setTimeStamp(System.currentTimeMillis() - initTimeStamp);
		trace.setType("unkown");
		if (effect.getElement() instanceof ChangeSceneEf) {
			trace.setType("ChangeScene");
			ChangeSceneEf changeScene = (ChangeSceneEf) effect.getElement();
			if (changeScene.getNextScene() != null) {
				trace.setArg1(changeScene.getNextScene().getId());
			}
		} else if (effect.getElement() instanceof ChangeFieldEf) {
			ChangeFieldEf changeField = (ChangeFieldEf) effect.getElement();
			String vars = "";
			String value = null;
			for (EAdField<?> f : changeField.getFields()) {
				if (value != null) {
					vars += ";";
				}

				vars += f.getVarDef().getName();

				if (value == null) {
					Object v = valueMap.getValue(f);
					value = v == null ? "" : v.toString();
				}
				trace.setType("ChangeField");
				trace.setArg1(vars);
				trace.setArg2(value);
			}

		}
		return trace;
	}

	public void stop() {
		if (this.isTracking() && tracker != null) {
			tracker.stopTracking();
		}
		super.stop();
	}

}
