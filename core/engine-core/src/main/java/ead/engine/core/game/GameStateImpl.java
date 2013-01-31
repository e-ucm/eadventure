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

package ead.engine.core.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.interfaces.features.Variabled;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.variables.EAdVarDef;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.factories.EffectGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.effects.EffectGO;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.tracking.GameTracker;

@Singleton
public class GameStateImpl implements GameState {

	private static Logger logger = LoggerFactory.getLogger("GameState");

	/**
	 * Game tracker
	 */
	private GameTracker tracker;

	/**
	 * Value map
	 */
	private ValueMap valueMap;

	/**
	 * Evaluator factory
	 */
	private EvaluatorFactory evaluatorFactory;

	/**
	 * Operator factory
	 */
	private OperatorFactory operatorFactory;

	/**
	 * Scene Element factory
	 */
	private SceneElementGOFactory sceneElementFactory;

	/**
	 * Effects factory
	 */
	private EffectGOFactory effectFactory;

	/**
	 * A list with the current effects
	 */
	private List<EffectGO<?>> effects;

	/**
	 * If the game state is paused
	 */
	private boolean paused;

	// Auxiliary variable, to avoid new every loop
	private ArrayList<EffectGO<?>> finishedEffects;

	private GameStateData gameStateData;

	@Inject
	public GameStateImpl(SceneElementGOFactory sceneElementFactory,
			EffectGOFactory effectFactory, ValueMap valueMap,
			OperatorFactory operatorFactory, EvaluatorFactory evaluatorFactory,
			GameTracker tracker) {
		logger.info("Initializing GameState...");
		this.sceneElementFactory = sceneElementFactory;
		this.effectFactory = effectFactory;
		this.valueMap = valueMap;
		this.operatorFactory = operatorFactory;
		this.evaluatorFactory = evaluatorFactory;
		this.tracker = tracker;

		this.operatorFactory.init(this, evaluatorFactory);
		this.evaluatorFactory.init(this, operatorFactory);

		effects = new ArrayList<EffectGO<?>>();
		finishedEffects = new ArrayList<EffectGO<?>>();
	}

	@Override
	public <T extends EAdCondition> boolean evaluate(T condition) {
		return evaluatorFactory.evaluate(condition);
	}

	public <T extends EAdOperation, S> S operate(Class<S> eAdVar, T eAdOperation) {
		return operatorFactory.operate(eAdVar, eAdOperation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.GameState#getEffects()
	 */
	@Override
	public List<EffectGO<?>> getEffects() {
		return effects;
	}

	@Override
	public void clearEffects(boolean clearPersistents) {
		for (EffectGO<?> effect : this.getEffects()) {
			if (!effect.getElement().isPersistent() || clearPersistents) {
				effect.stop();
			}
		}
	}

	@Override
	public void addEffect(EAdEffect e) {
		this.addEffect(e, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.GameState#addEffect(int,
	 * es.eucm.eadventure.common.model.effects.EAdEffect)
	 */
	@Override
	public EffectGO<?> addEffect(EAdEffect e, Event action,
			EAdSceneElement parent) {
		if (e != null) {
			if (evaluatorFactory.evaluate(e.getCondition())) {
				EffectGO<?> effectGO = effectFactory.get(e);
				if (effectGO == null) {
					logger.warn("No game object for effect {}", e.getClass());
					return null;
				}
				effectGO.setGUIAction(action);
				effectGO.setParent(parent);
				effectGO.initialize();
				if (effectGO.isQueueable()) {
					tracker.track(effectGO);
					effects.add(effectGO);
				} else {
					effectGO.finish();
					tracker.track(effectGO);
				}
				return effectGO;
			} else if (e.isNextEffectsAlways()) {
				for (EAdEffect ne : e.getNextEffects())
					addEffect(ne);
			}
		}
		return null;

	}

	public void update(float delta) {
		if (!isPaused()) {
			effects = getEffects();
			finishedEffects.clear();
			boolean block = false;
			int i = 0;
			while (i < getEffects().size()) {
				EffectGO<?> effectGO = effects.get(i);
				i++;

				if (block)
					continue;

				if (effectGO.isStopped() || effectGO.isFinished()) {
					finishedEffects.add(effectGO);
					if (effectGO.isFinished())
						effectGO.finish();
				} else {
					if (effectGO.isBlocking())
						// If effect is blocking, get out of the loop
						block = true;

					effectGO.act(delta);
				}

			}

			// Delete finished effects
			for (EffectGO<?> e : finishedEffects) {
				// logger.info("Finished or discarded effect {}", e.getClass());
				getEffects().remove(e);
			}
		}
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void saveState() {
		ArrayList<EAdEffect> effectsList = new ArrayList<EAdEffect>();
		for (EffectGO<?> effGO : effects) {
			effectsList.add(effGO.getElement());
		}

		//		Stack<EAdScene> stack = new Stack<EAdScene>();

		//		Map<EAdVarDef<?>, Object> systemVars = new HashMap<EAdVarDef<?>, Object>();
		// systemVars.putAll(valueMap.getSystemVars());

		// Map<Variabled, Map<EAdVarDef<?>, Object>> originalElementVars =
		// valueMap
		// .getElementVars();
		//		Map<Variabled, Map<EAdVarDef<?>, Object>> elementVars = new HashMap<Variabled, Map<EAdVarDef<?>, Object>>();
		// for (Entry<Variabled, Map<EAdVarDef<?>, Object>> entry :
		// originalElementVars
		// .entrySet()) {
		// Map<EAdVarDef<?>, Object> map = new HashMap<EAdVarDef<?>, Object>();
		// map.putAll(entry.getValue());
		// elementVars.put(entry.getKey(), map);
		// }

		//		ArrayList<Variabled> updateList = new ArrayList<Variabled>();
		// updateList.addAll(valueMap.getUpdateList());

	}

	private GameStateData clone(GameStateData state) {
		ArrayList<EAdEffect> effectsList = new ArrayList<EAdEffect>();
		for (EAdEffect eff : state.getEffects()) {
			effectsList.add(eff);
		}

		Stack<EAdScene> stack = new Stack<EAdScene>();
		for (EAdScene s : state.getPreviousSceneStack()) {
			stack.add(s);
		}

		Map<EAdVarDef<?>, Object> systemVars = new HashMap<EAdVarDef<?>, Object>();
		systemVars.putAll(state.getSystemVars());

		Map<Variabled, Map<EAdVarDef<?>, Object>> originalElementVars = state
				.getElementVars();
		Map<Variabled, Map<EAdVarDef<?>, Object>> elementVars = new HashMap<Variabled, Map<EAdVarDef<?>, Object>>();
		for (Entry<Variabled, Map<EAdVarDef<?>, Object>> entry : originalElementVars
				.entrySet()) {
			Map<EAdVarDef<?>, Object> map = new HashMap<EAdVarDef<?>, Object>();
			map.putAll(entry.getValue());
			elementVars.put(entry.getKey(), map);
		}

		ArrayList<Variabled> updateList = new ArrayList<Variabled>();
		updateList.addAll(state.getUpdateList());

		return new GameStateData(state.getScene(), effectsList, stack,
				systemVars, elementVars, updateList);
	}

	public void loadState() {
		if (gameStateData == null) {
			logger.info("No state saved.");
		} else {
			GameStateData gameStateData = clone(this.gameStateData);
			sceneElementFactory.remove(gameStateData.getScene());

			// FIXME this will fail in some cases (when the effect depend on an
			// InputAction, for example
			for (EAdEffect effect : gameStateData.getEffects()) {
				this.addEffect(effect);
			}

			// valueMap.setElementVars(gameStateData.getElementVars());
			// valueMap.setUpdateList(gameStateData.getUpdateList());
			// valueMap.getUpdateList().addAll(
			// gameStateData.getElementVars().keySet());
			// valueMap.setSystemVars(gameStateData.getSystemVars());

		}
	}

	@Override
	public void setValue(EAdField<?> var, EAdOperation operation) {
		setValue(var.getElement(), var.getVarDef(), operation);
	}

	@Override
	public void setValue(Object element, EAdVarDef<?> var,
			EAdOperation operation) {
		Object result = operatorFactory.operate(var.getType(), operation);
		setValue(element, var, result);
	}

	@Override
	public void setValue(EAdField<?> field, Object value) {
		valueMap.setValue(field, value);

	}

	@Override
	public void setValue(Object element, EAdVarDef<?> varDef, Object value) {
		valueMap.setValue(element, varDef, value);
	}

	@Override
	public <S> S getValue(EAdField<S> field) {
		return valueMap.getValue(field);
	}

	@Override
	public <S> S getValue(Object element, EAdVarDef<S> varDef) {
		return valueMap.getValue(element, varDef);
	}

	@Override
	public Map<EAdVarDef<?>, Object> getElementVars(Object element) {
		return valueMap.getElementVars(element);
	}

	@Override
	public Object maybeDecodeField(Object element) {
		return valueMap.maybeDecodeField(element);
	}

	@Override
	public boolean checkForUpdates(Object element) {
		return valueMap.checkForUpdates(element);
	}

	@Override
	public void setUpdateListEnable(boolean enable) {
		valueMap.setUpdateListEnable(enable);
	}

	@Override
	public void remove(Object element) {
		valueMap.remove(element);
	}

}
