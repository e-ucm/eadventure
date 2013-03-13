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

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.interfaces.features.Variabled;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.variables.EAdVarDef;
import ead.engine.core.factories.EffectGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.effects.EffectGO;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.tracking.GameTracker;
import ead.tools.StringHandler;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class GameStateImpl extends ValueMapImpl implements GameState,
		TweenAccessor<EAdField<?>> {

	private static final char BEGIN_VAR_CHAR = '[';

	private static final char END_VAR_CHAR = ']';

	private static final char BEGIN_CONDITION_CHAR = '(';

	private static final char END_CONDITION_CHAR = ')';

	/**
	 * Operator factory
	 */
	protected OperatorFactory operatorFactory;

	/**
	 * Game tracker
	 */
	private GameTracker tracker;

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

	private TweenManager tweenManager;

	private GameStateData gameStateData;

	@Inject
	public GameStateImpl(StringHandler stringHandler,
			SceneElementGOFactory sceneElementFactory,
			EffectGOFactory effectFactory,
			ReflectionProvider reflectionProvider, GameTracker tracker) {
		super(reflectionProvider, stringHandler);
		this.sceneElementFactory = sceneElementFactory;
		this.effectFactory = effectFactory;
		this.tracker = tracker;
		this.operatorFactory = new OperatorFactory(reflectionProvider, this,
				stringHandler);

		effects = new ArrayList<EffectGO<?>>();
		finishedEffects = new ArrayList<EffectGO<?>>();

		// Init tween manager
		this.tweenManager = new TweenManager();
		Tween.registerAccessor(EAdField.class, this);
		Tween.registerAccessor(BasicField.class, this);
	}

	@Override
	public <T extends EAdCondition> boolean evaluate(T condition) {
		if (condition == null) {
			return true;
		}
		return operatorFactory.operate(Boolean.class, condition);
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
			if (evaluate(e.getCondition())) {
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
					effectFactory.remove(effectGO);
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
			// Tween manager
			tweenManager.update(delta);

			// Effects
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
				effects.remove(e);
				effectFactory.remove(e);
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

		// Stack<EAdScene> stack = new Stack<EAdScene>();

		// Map<EAdVarDef<?>, Object> systemVars = new HashMap<EAdVarDef<?>,
		// Object>();
		// systemVars.putAll(valueMap.getSystemVars());

		// Map<Variabled, Map<EAdVarDef<?>, Object>> originalElementVars =
		// valueMap
		// .getElementVars();
		// Map<Variabled, Map<EAdVarDef<?>, Object>> elementVars = new
		// HashMap<Variabled, Map<EAdVarDef<?>, Object>>();
		// for (Entry<Variabled, Map<EAdVarDef<?>, Object>> entry :
		// originalElementVars
		// .entrySet()) {
		// Map<EAdVarDef<?>, Object> map = new HashMap<EAdVarDef<?>, Object>();
		// map.putAll(entry.getValue());
		// elementVars.put(entry.getKey(), map);
		// }

		// ArrayList<Variabled> updateList = new ArrayList<Variabled>();
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
	public <S> void setValue(EAdField<S> var, EAdOperation operation) {
		setValue(var.getElement(), var.getVarDef(), operation);
	}

	@Override
	public <S> void setValue(Object element, EAdVarDef<S> var,
			EAdOperation operation) {
		S result = operatorFactory.operate(var.getType(), operation);
		setValue(element, var, result);
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}

	@Override
	public int getValues(EAdField<?> field, int type, float[] values) {
		switch (type) {
		default:
			Object o = getValue(field);
			if (o instanceof Number) {
				values[0] = ((Number) o).floatValue();
				return 1;
			} else {
				return 0;
			}
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public void setValues(EAdField field, int type, float[] values) {
		switch (type) {
		default:
			if (field.getVarDef().getType() == Float.class) {
				setValue(field, values[0]);
			} else if (field.getVarDef().getType() == Integer.class) {
				setValue(field, (int) values[0]);
			}
		}

	}

	//---------------------------------//
	// TEXT PROCESSING
	//---------------------------------//

	public String processTextVars(String text, EAdList<EAdOperation> operations) {
		text = processConditionalExpressions(text, operations);
		return processVars(text, operations);
	}

	private String processVars(String text, EAdList<EAdOperation> operations) {
		int i = 0;
		boolean done = false;
		while (i < text.length() && !done) {
			i = text.indexOf(BEGIN_VAR_CHAR, i);
			if (i != -1) {
				int separatorIndex = text.indexOf(END_VAR_CHAR, i + 1);
				if (separatorIndex != -1) {
					String varName = text.substring(i + 1, separatorIndex);
					Integer index = new Integer(varName);
					Object o = operatorFactory.operate(Object.class, operations
							.get(index));
					if (o != null) {
						String value = o instanceof EAdString ? stringHandler
								.getString((EAdString) o) : o.toString();
						text = text.substring(0, i) + value
								+ text.substring(separatorIndex + 1);
					}
				}
				i = separatorIndex;

			} else {
				done = true;
			}
		}
		return text;
	}

	private String processConditionalExpressions(String text,
			EAdList<EAdOperation> fields) {
		String newText = "";
		if (text != null) {
			int i = 0;
			boolean finished = false;
			while (!finished && i < text.length()) {
				int beginCondition = text.indexOf(BEGIN_CONDITION_CHAR, i);
				int endCondition = text.indexOf(END_CONDITION_CHAR,
						beginCondition);
				if (beginCondition != -1 && endCondition != -1
						&& endCondition > beginCondition) {
					String condition = text.substring(beginCondition + 1,
							endCondition);
					String result = evaluateExpression(condition, fields);
					newText += text.substring(i, beginCondition) + result;
					i = endCondition + 1;
				} else {
					newText += text.substring(i);
					finished = true;
				}
			}
		}
		return newText;
	}

	/**
	 * Evaluates conditional expressions (#boolean_var? value_1 : value_2 )
	 * 
	 * @param expression
	 * @return
	 */
	private String evaluateExpression(String expression,
			EAdList<EAdOperation> operations) {

		if (expression.contains("?") && expression.contains(":")) {
			int questionMark = expression.indexOf('?');
			int points = expression.indexOf(':');
			String condition = expression.substring(0, questionMark);
			String trueValue = expression.substring(questionMark + 1, points);
			String falseValue = expression.substring(points + 1, expression
					.length());

			int beginVar = condition.indexOf(BEGIN_VAR_CHAR);
			int endVar = condition.indexOf(END_VAR_CHAR);
			if (beginVar != -1 && endVar != -1 && endVar > beginVar) {

				Integer indexCondition = 0;
				String varName = "";
				try {
					varName = expression.substring(beginVar + 1, endVar);
					indexCondition = new Integer(varName);
				} catch (NumberFormatException e) {
					logger.warn(varName + " is not a valid index in "
							+ expression);
					return BEGIN_CONDITION_CHAR + expression
							+ END_CONDITION_CHAR;
				}

				Object o = operatorFactory.operate(Object.class, operations
						.get(indexCondition));

				if (o != null && o instanceof Boolean) {
					Boolean b = (Boolean) o;
					if (b.booleanValue())
						return trueValue;
					else
						return falseValue;
				} else if (o != null && o instanceof Number) {
					Number n = (Number) o;
					if (n.floatValue() != 0)
						return trueValue;
					else
						return falseValue;
				}
				return falseValue;
			}
		}

		return BEGIN_CONDITION_CHAR + expression + END_CONDITION_CHAR;
	}
}
