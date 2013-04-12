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

package ead.json.reader;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.StringMap;

import ead.common.model.assets.multimedia.Sound;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.effects.AddChildEf;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.common.model.elements.effects.QuitGameEf;
import ead.common.model.elements.effects.RemoveEf;
import ead.common.model.elements.effects.ToggleSoundEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.timedevents.WaitEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.predef.effects.OneShotEf;
import ead.common.model.elements.predef.effects.SpeakSceneElementEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.transitions.ScaleTransition;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.text.EAdString;
import ead.reader.model.ObjectsFactory;

@SuppressWarnings("unchecked")
public class EffectsReader {

	private static final Logger logger = LoggerFactory
			.getLogger("EffectsReader");

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

	public EAdEffect read(StringMap<Object> e) {
		EAdEffect effect = null;
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
				effect = new QuitGameEf();
			}

			Boolean oneshot = (Boolean) e.get("oneshot");
			if (oneshot != null && oneshot.booleanValue()) {
				effect = new OneShotEf(effect);
			}

			StringMap<Object> cond = (StringMap<Object>) e.get("cond");
			if (cond != null) {
				EAdCondition condition = conditionsReader.read(cond);
				effect.setCondition(condition);
			}

			String id = (String) e.get("id");
			if (id != null) {
				effect.setId(id);
			}

			Boolean persistent = (Boolean) e.get("persistent");
			if (persistent != null) {
				effect.setPersistent(persistent.booleanValue());
			}

			Boolean nextEffectsAlways = (Boolean) e.get("nextEffectsAlways");
			effect.setNextEffectsAlways(nextEffectsAlways != null
					&& nextEffectsAlways.booleanValue());

			objectsFactory.putEAdElement(effect.getId(), effect);
			addNextEffects(effect, e);
		} catch (Exception ex) {
			logger.error("Error reading {}", e, ex);
		}
		return effect;
	}

	private EAdEffect getGoToPosition(StringMap<Object> e) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setXtarget(new ValueOp(((Number) e.get("x")).floatValue()));
		move.setYtarget(new ValueOp(((Number) e.get("y")).floatValue()));
		EAdSceneElement element = (EAdSceneElement) objectsFactory
				.getEAdElement((String) e.get("sceneElement"));
		move.setSceneElement(element);
		move.setUseTrajectory(false);
		return move;
	}

	private EAdEffect getOneShot(StringMap<Object> e) {
		EAdEffect effect = this.read((StringMap<Object>) e.get("effect"));
		return new OneShotEf(effect);
	}

	private EAdEffect getGoTo(StringMap<Object> e) {
		MoveSceneElementEf effect = new MoveSceneElementEf();
		effect.setUseTrajectory(true);
		String target = (String) e.get("target");
		if (target != null) {
			EAdElement element = (EAdElement) objectsFactory
					.getEAdElement(target);
			effect.setSceneElement(element);
		} else {
			effect.setSceneElement(SystemFields.ACTIVE_ELEMENT);
		}
		String sceneElement = (String) e.get("sceneElement");
		if (sceneElement != null)
			effect.setTarget((EAdSceneElement) objectsFactory
					.getEAdElement(sceneElement));
		return effect;
	}

	private EAdEffect getRemove(StringMap<Object> e) {
		String target = (String) e.get("target");
		return new RemoveEf((EAdSceneElement) objectsFactory
				.getEAdElement(target));
	}

	private EAdEffect getAddChild(StringMap<Object> e) {
		String parent = (String) e.get("parent");
		EAdSceneElement sceneElement = sceneReader
				.parseSceneElement((StringMap<Object>) e.get("sceneElement"));
		return new AddChildEf(parent, sceneElement);
	}

	private EAdEffect getTriggerMacro(StringMap<Object> e) {
		TriggerMacroEf triggerMacro = new TriggerMacroEf();
		Collection<StringMap<Object>> conditions = (Collection<StringMap<Object>>) e
				.get("conditions");
		Collection<String> effects = (Collection<String>) e.get("effects");
		Iterator<String> i = effects.iterator();
		for (StringMap<Object> c : conditions) {
			EAdCondition cond = conditionsReader.read(c);
			EAdEffect effect = (EAdEffect) objectsFactory.getEAdElement(i
					.next());
			triggerMacro.putEffect(effect, cond);
		}
		return triggerMacro;
	}

	private EAdEffect getSpeak(StringMap<Object> e) {
		String string = (String) e.get("string");
		String sceneElement = (String) e.get("sceneElement");
		EAdElement element = null;
		if (sceneElement != null) {
			element = (EAdElement) objectsFactory.getEAdElement(sceneElement);
		}
		SpeakSceneElementEf speak = new SpeakSceneElementEf(element,
				new EAdString(string));
		String font = (String) e.get("font");
		if (font != null) {
			speak.setFont((EAdFont) objectsFactory.getAsset(font));
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

	private EAdEffect getSequenceRef(StringMap<Object> e) {
		EAdEffect first = null;
		EAdEffect effect = null;
		Collection<String> ef = (Collection<String>) e.get("sequence");
		for (String eff : ef) {
			EAdEffect nextEffect = (EAdEffect) objectsFactory
					.getEAdElement(eff);
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

	private EAdEffect getSequence(StringMap<Object> e) {
		EAdEffect first = null;
		EAdEffect effect = null;
		Collection<StringMap<Object>> ef = (Collection<StringMap<Object>>) e
				.get("sequence");
		for (StringMap<Object> eff : ef) {
			EAdEffect nextEffect = read(eff);
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

	private EAdEffect getRef(StringMap<Object> e) {
		String ref = (String) e.get("ref");
		EAdEffect reference = (EAdEffect) objectsFactory.getEAdElement(ref);
		if (reference == null) {
			logger.warn("Reference to effect {} not found", ref);
		}
		return reference;
	}

	private EAdEffect getWait(StringMap<Object> e) {
		return new WaitEf(((Number) e.get("time")).intValue());
	}

	private EAdEffect getPlaySound(StringMap<Object> e) {
		Boolean background = (Boolean) e.get("background");
		String uri = (String) e.get("uri");
		return new PlaySoundEf(uri == null ? null : new Sound(uri),
				background != null && background.booleanValue());
	}

	private EAdEffect getChangeScene(StringMap<Object> e) {
		StringMap<Object> t = (StringMap<Object>) e.get("transition");
		EAdTransition transition = EmptyTransition.instance();
		if (t != null) {
			Number time = (Number) t.get("time");
			if (t.get("type").equals("fadein")) {
				transition = new FadeInTransition(time.intValue());
			} else if (t.get("type").equals("scale")) {
				Boolean grow = (Boolean) t.get("grow");
				Boolean targetNext = (Boolean) t.get("targetNext");
				transition = new ScaleTransition(time.intValue(), grow != null
						&& grow.booleanValue(), targetNext != null
						&& targetNext.booleanValue());
			}
		}
		String ns = (String) e.get("nextScene");
		EAdElement nextScene = null;
		if (ns != null) {
			nextScene = (EAdElement) objectsFactory.getEAdElement(ns);
			if (nextScene == null) {
				nextScene = new BasicElement(ns);
			}
		}
		ChangeSceneEf changeScene = new ChangeSceneEf(nextScene, transition);
		return changeScene;
	}

	private void addNextEffects(EAdEffect effect, StringMap<Object> e) {
		Collection<Object> nextEffects = (Collection<Object>) e
				.get("nextEffects");
		if (nextEffects != null) {
			for (Object o : nextEffects) {
				EAdEffect nextEffect = read((StringMap<Object>) o);
				effect.getNextEffects().add(nextEffect);
			}
		}
		Collection<Object> simultaneousEffects = (Collection<Object>) e
				.get("simultaneousEffects");
		if (simultaneousEffects != null) {
			for (Object o : simultaneousEffects) {
				EAdEffect sEffect = read((StringMap<Object>) o);
				effect.getSimultaneousEffects().add(sEffect);
			}
		}
	}

	private EAdEffect getChangeField(StringMap<Object> e) {
		ChangeFieldEf changeField = new ChangeFieldEf();
		EAdOperation operation = operationReader.read((StringMap<Object>) e
				.get("operation"));
		Collection<String> fields = (Collection<String>) e.get("fields");
		for (String f : fields) {
			changeField.addField(operationReader.translateField(f));
		}
		changeField.setOperation(operation);

		return changeField;
	}

	private EAdEffect getInterpolation(StringMap<Object> e) {
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
			interpolation.setRelative(relative.booleanValue());
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
