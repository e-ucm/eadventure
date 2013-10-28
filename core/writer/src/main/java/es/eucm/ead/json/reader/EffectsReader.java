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

package es.eucm.ead.json.reader;

import com.google.gson.internal.StringMap;
import es.eucm.ead.model.assets.multimedia.Music;
import es.eucm.ead.model.assets.multimedia.Sound;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.effects.AddChildEf;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.EmptyEffect;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.PlayMusicEf;
import es.eucm.ead.model.elements.effects.PlaySoundEf;
import es.eucm.ead.model.elements.effects.QuitGameEf;
import es.eucm.ead.model.elements.effects.RemoveEf;
import es.eucm.ead.model.elements.effects.TogglePauseEf;
import es.eucm.ead.model.elements.effects.ToggleSoundEf;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.ead.model.elements.effects.text.QuestionEf;
import es.eucm.ead.model.elements.effects.timedevents.WaitEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.predef.effects.OneShotEf;
import es.eucm.ead.model.elements.predef.effects.SpeakSceneElementEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.transitions.EmptyTransition;
import es.eucm.ead.model.elements.transitions.FadeInTransition;
import es.eucm.ead.model.elements.transitions.ScaleTransition;
import es.eucm.ead.model.elements.transitions.Transition;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.reader.ObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class EffectsReader {

	static private Logger logger = LoggerFactory.getLogger(EffectsReader.class);

	private ObjectsFactory objectsFactory;

	private OperationReader operationReader;

	private ConditionReader conditionsReader;

	private TemplateReader templateReader;

	private SceneReader sceneReader;

	public EffectsReader(ObjectsFactory objectsFactory,
			OperationReader operationReader, ConditionReader conditionsReader,
			TemplateReader templateReader, SceneReader sceneReader) {
		this.objectsFactory = objectsFactory;
		this.operationReader = operationReader;
		this.conditionsReader = conditionsReader;
		this.templateReader = templateReader;
		this.sceneReader = sceneReader;
	}

	public Effect read(StringMap<Object> e) {
		Effect effect = null;
		String type = (String) e.get("type");
		if (type == null) {
			logger.error("Effect without type {}", e);
		}

		try {
			if (type.equals("interpolation")) {
				effect = getInterpolation(e);
			} else if (type.equals("changefield")) {
				effect = getChangeField(e);
			} else if (type.equals("changescene")) {
				effect = getChangeScene(e);
			} else if (type.equals("playsound")) {
				effect = getPlaySound(e);
			} else if (type.equals("wait")) {
				effect = getWait(e);
			} else if (type.equals("ref")) {
				effect = getRef(e);
			} else if (type.equals("sequence")) {
				effect = getSequence(e);
			} else if (type.equals("sequenceref")) {
				effect = getSequenceRef(e);
			} else if (type.equals("speak")) {
				effect = getSpeak(e);
			} else if (type.equals("triggermacro")) {
				effect = getTriggerMacro(e);
			} else if (type.equals("addchild")) {
				effect = getAddChild(e);
			} else if (type.equals("remove")) {
				effect = getRemove(e);
			} else if (type.equals("goto")) {
				effect = getGoTo(e);
			} else if (type.equals("oneshot")) {
				effect = getOneShot(e);
			} else if (type.equals("stopgoto")) {
				effect = getGoTo(e);
				((MoveSceneElementEf) effect).setXtarget(null);
				((MoveSceneElementEf) effect).setYtarget(null);
			} else if (type.equals("gotoposition")) {
				effect = getGoToPosition(e);
			} else if (type.equals("togglesound")) {
				effect = new ToggleSoundEf();
			} else if (type.equals("quit")) {
				effect = getQuit(e);
			} else if (type.equals("question")) {
				effect = getQuestion(e);
			} else if (type.equals("togglepause")) {
				effect = new TogglePauseEf();
			}

			Boolean oneshot = (Boolean) e.get("oneshot");
			if (oneshot != null && oneshot) {
				effect = new OneShotEf(effect);
			}

			StringMap<Object> cond = (StringMap<Object>) e.get("cond");
			if (cond != null) {
				Condition condition = conditionsReader.read(cond);
				effect.setCondition(condition);
			}

			String id = (String) e.get("id");
			if (id != null) {
				effect.setId(id);
			}

			Boolean persistent = (Boolean) e.get("persistent");
			if (persistent != null) {
				effect.setPersistent(persistent);
			}

			Boolean nextEffectsAlways = (Boolean) e.get("nextEffectsAlways");
			effect.setNextEffectsAlways(nextEffectsAlways != null
					&& nextEffectsAlways);

			objectsFactory.putIdentified(effect);
			addNextEffects(effect, e);
		} catch (Exception ex) {
			logger.error("Error reading {}", e, ex);
		}
		return effect;
	}

	private Effect getQuit(StringMap<Object> e) {
		QuitGameEf effect = new QuitGameEf();
		Boolean restart = (Boolean) e.get("restart");
		effect.setRestart(restart != null ? restart : false);
		return effect;
	}

	private Effect getQuestion(StringMap<Object> e) {
		String question = (String) e.get("question");
		QuestionEf effect = new QuestionEf();
		effect.setQuestion(new EAdString(question));
		Collection<StringMap<Object>> answers = (Collection<StringMap<Object>>) e
				.get("answers");
		for (StringMap<Object> a : answers) {
			EAdString text = new EAdString((String) a.get("string"));
			EAdList<Effect> effects = new EAdList<Effect>();
			Collection<StringMap<Object>> effs = ((Collection<StringMap<Object>>) a
					.get("effects"));
			for (StringMap<Object> ef : effs) {
				effects.add(read(ef));
			}
			// A empty list is ignored by the writer
			if (effects.isEmpty()) {
				effects.add(new EmptyEffect());
			}
			effect.addAnswer(text, effects);
		}
		return effect;
	}

	private Effect getGoToPosition(StringMap<Object> e) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setXtarget(new ValueOp(((Number) e.get("x")).floatValue()));
		move.setYtarget(new ValueOp(((Number) e.get("y")).floatValue()));
		SceneElement element = (SceneElement) objectsFactory
				.getObjectById((String) e.get("sceneElement"));
		move.setSceneElement(element);
		Boolean useTrajectory = (Boolean) e.get("useTrajectory");
		move.setUseTrajectory(useTrajectory == null ? false : useTrajectory);
		return move;
	}

	private Effect getOneShot(StringMap<Object> e) {
		Effect effect = this.read((StringMap<Object>) e.get("effect"));
		return new OneShotEf(effect);
	}

	private Effect getGoTo(StringMap<Object> e) {
		MoveSceneElementEf effect = new MoveSceneElementEf();
		effect.setUseTrajectory(true);
		String target = (String) e.get("target");
		if (target != null) {
			BasicElement element = (BasicElement) objectsFactory
					.getObjectById(target);
			effect.setSceneElement(element);
		} else {
			effect.setSceneElement(SystemFields.ACTIVE_ELEMENT);
		}
		String sceneElement = (String) e.get("sceneElement");
		if (sceneElement != null)
			effect.setTarget((SceneElement) objectsFactory
					.getObjectById(sceneElement));
		return effect;
	}

	private Effect getRemove(StringMap<Object> e) {
		String target = (String) e.get("target");
		return new RemoveEf((SceneElement) objectsFactory.getObjectById(target));
	}

	private Effect getAddChild(StringMap<Object> e) {
		String parent = (String) e.get("parent");
		SceneElement sceneElement = sceneReader
				.parseSceneElement((StringMap<Object>) e.get("sceneElement"));
		return new AddChildEf(parent, sceneElement);
	}

	private Effect getTriggerMacro(StringMap<Object> e) {
		TriggerMacroEf triggerMacro = new TriggerMacroEf();
		Collection<StringMap<Object>> conditions = (Collection<StringMap<Object>>) e
				.get("conditions");
		Collection<String> effects = (Collection<String>) e.get("effects");
		Iterator<String> i = effects.iterator();
		for (StringMap<Object> c : conditions) {
			Condition cond = conditionsReader.read(c);
			Effect effect = (Effect) objectsFactory.getObjectById(i.next());
			triggerMacro.putEffect(cond, effect);
		}
		return triggerMacro;
	}

	private Effect getSpeak(StringMap<Object> e) {
		String string = (String) e.get("string");
		String sceneElement = (String) e.get("sceneElement");
		BasicElement element = null;
		if (sceneElement != null) {
			element = (BasicElement) objectsFactory.getObjectById(sceneElement);
		}
		SpeakSceneElementEf speak = new SpeakSceneElementEf(element,
				new EAdString(string));
		String font = (String) e.get("font");
		if (font != null) {
			speak.setFont((EAdFont) objectsFactory.getObjectById(font));
		}
		String tp = (String) e.get("textPaint");
		String bp = (String) e.get("bubblePaint");

		EAdPaint textPaint = ColorFill.BLACK;
		EAdPaint bubblePaint = Paint.BLACK_ON_WHITE;
		if (tp != null) {
			textPaint = objectsFactory.getPaint(tp);
		}

		if (bp != null) {
			bubblePaint = objectsFactory.getPaint(bp);
		}

		Collection<StringMap<Object>> ops = (Collection<StringMap<Object>>) e
				.get("operations");
		if (ops != null)
			for (StringMap<Object> op : ops) {
				speak.getCaption().getOperations()
						.add(operationReader.read(op));
			}

		Number time = (Number) e.get("time");
		if (time != null) {
			speak.setTime(time.intValue());
		}
		speak.setColor(textPaint, bubblePaint);
		return speak;
	}

	private Effect getSequenceRef(StringMap<Object> e) {
		Effect first = null;
		Effect effect = null;
		Collection<String> ef = (Collection<String>) e.get("sequence");
		for (String eff : ef) {
			Effect nextEffect = (Effect) objectsFactory.getObjectById(eff);
			if (first == null) {
				first = nextEffect;
				effect = first;
			} else {
				effect.getNextEffects().add(nextEffect);
				effect = nextEffect;
			}
		}
		return first;
	}

	private Effect getSequence(StringMap<Object> e) {
		Effect first = null;
		Effect effect = null;
		Collection<StringMap<Object>> ef = (Collection<StringMap<Object>>) e
				.get("sequence");
		for (StringMap<Object> eff : ef) {
			Effect nextEffect = read(eff);
			if (first == null) {
				first = nextEffect;
				effect = first;
			} else {
				effect.getNextEffects().add(nextEffect);
				effect = nextEffect;
			}
		}
		return first;
	}

	private Effect getRef(StringMap<Object> e) {
		String ref = (String) e.get("ref");
		Effect reference = (Effect) objectsFactory.getObjectById(ref);
		if (reference == null) {
			logger.warn("Reference to effect {} not found", ref);
		}
		return reference;
	}

	private Effect getWait(StringMap<Object> e) {
		WaitEf effect = new WaitEf();
		Boolean waitUntilClick = (Boolean) e.get("waitUntilClick");
		if (waitUntilClick != null) {
			effect.setWaitUntilClick(waitUntilClick);
		}

		Number time = (Number) e.get("time");
		if (time != null) {
			effect.setTime(time.intValue());
		}

		Boolean blockInput = (Boolean) e.get("blockInput");
		if (blockInput != null) {
			effect.setBlockInput(blockInput);
		}
		return effect;
	}

	private Effect getPlaySound(StringMap<Object> e) {
		Boolean background = (Boolean) e.get("background");
		String uri = (String) e.get("uri");
		if (background != null && background) {
			return new PlayMusicEf(uri == null ? null : new Music(uri), 1.0f,
					true);
		} else {
			return new PlaySoundEf(uri == null ? null : new Sound(uri));
		}
	}

	private Effect getChangeScene(StringMap<Object> e) {
		StringMap<Object> t = (StringMap<Object>) e.get("transition");
		Transition transition = EmptyTransition.instance();
		if (t != null) {
			Number time = (Number) t.get("time");
			if (t.get("type").equals("fadein")) {
				transition = new FadeInTransition(time.intValue());
			} else if (t.get("type").equals("scale")) {
				Boolean grow = (Boolean) t.get("grow");
				Boolean targetNext = (Boolean) t.get("targetNext");
				transition = new ScaleTransition(time.intValue(), grow != null
						&& grow, targetNext != null && targetNext);
			}
		}
		String ns = (String) e.get("nextScene");
		BasicElement nextScene = null;
		if (ns != null) {
			nextScene = (BasicElement) objectsFactory.getObjectById(ns);
			if (nextScene == null) {
				nextScene = new BasicElement(ns);
			}
		}
		return new ChangeSceneEf(nextScene, transition);
	}

	private void addNextEffects(Effect effect, StringMap<Object> e) {
		Collection<Object> nextEffects = (Collection<Object>) e
				.get("nextEffects");
		if (nextEffects != null) {
			for (Object o : nextEffects) {
				Effect nextEffect = read((StringMap<Object>) o);
				effect.getNextEffects().add(nextEffect);
			}
		}
		Collection<Object> simultaneousEffects = (Collection<Object>) e
				.get("simultaneousEffects");
		if (simultaneousEffects != null) {
			for (Object o : simultaneousEffects) {
				Effect sEffect = read((StringMap<Object>) o);
				effect.getSimultaneousEffects().add(sEffect);
			}
		}
	}

	private Effect getChangeField(StringMap<Object> e) {
		ChangeFieldEf changeField = new ChangeFieldEf();
		Operation operation = operationReader.read((StringMap<Object>) e
				.get("operation"));
		Collection<String> fields = (Collection<String>) e.get("fields");
		for (String f : fields) {
			// XXX only takes the last field in the list
			ElementField field = operationReader.translateField(f);
			changeField.setElement(field.getElement());
			changeField.setVarName(field.getVarName());
		}
		changeField.setOperation(operation);

		return changeField;
	}

	private Effect getInterpolation(StringMap<Object> e) {
		InterpolationEf interpolation = new InterpolationEf();
		Number start = (Number) e.get("start");
		Number end = (Number) e.get("end");
		Number time = (Number) e.get("time");
		Collection<String> fields = (Collection<String>) e.get("fields");
		for (String f : fields) {
			interpolation.addField(operationReader.translateField(f),
					new ValueOp(start.floatValue()), new ValueOp(end
							.floatValue()));
		}

		Boolean relative = (Boolean) e.get("relative");
		if (relative != null) {
			interpolation.setRelative(relative);
		}

		interpolation.setInterpolationTime(time.intValue());
		return interpolation;
	}

	public void readEffects(Collection<StringMap<Object>> effects) {
		for (StringMap<Object> e : effects) {
			templateReader.applyTemplates(e);
			read(e);
		}
	}

}
