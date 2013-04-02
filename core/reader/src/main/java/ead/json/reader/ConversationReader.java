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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.StringMap;

import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.text.QuestionEf;
import ead.common.model.elements.predef.effects.SpeakSceneElementEf;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.text.EAdString;
import ead.reader.model.ObjectsFactory;

@SuppressWarnings("unchecked")
public class ConversationReader {

	private Logger logger = LoggerFactory.getLogger("ConversationReader");

	private Map<String, EAdEffect> segments;
	private Map<String, EAdPaint> texts;
	private Map<String, EAdPaint> bubbles;
	private Map<EAdString, String> strings;
	private ObjectsFactory objectsFactory;
	private EffectsReader effectsReader;
	private ConditionReader conditionsReader;
	private int id;
	private String currentId;
	private TemplateReader templateReader;

	private EAdFont font;

	public ConversationReader(ObjectsFactory objectsFactory,
			EffectsReader effectsReader, ConditionReader conditionsReader,
			TemplateReader templateReader) {
		segments = new HashMap<String, EAdEffect>();
		bubbles = new HashMap<String, EAdPaint>();
		texts = new HashMap<String, EAdPaint>();
		strings = new HashMap<EAdString, String>();
		this.objectsFactory = objectsFactory;
		this.effectsReader = effectsReader;
		this.conditionsReader = conditionsReader;
		this.templateReader = templateReader;
	}

	public EAdEffect read(StringMap<Object> conv) {
		segments.clear();
		strings.clear();
		id = 0;
		currentId = (String) conv.get("id");
		Collection<StringMap<Object>> seg = (Collection<StringMap<Object>>) conv
				.get("segments");
		for (StringMap<Object> s : seg) {
			addSegment(s);
		}

		Collection<StringMap<Object>> links = (Collection<StringMap<Object>>) conv
				.get("links");
		for (StringMap<Object> l : links) {
			addLink(l);
		}

		for (StringMap<Object> s : seg) {
			addLinkQuestion(s);
		}
		EAdEffect effect = segments.get(conv.get("start"));
		effect.setId(currentId);
		objectsFactory.putEAdElement(effect.getId(), effect);
		return effect;
	}

	private void addLinkQuestion(StringMap<Object> s) {
		StringMap<Object> q = (StringMap<Object>) s.get("question");
		if (q != null) {
			String segmentId = (String) s.get("id");
			QuestionEf question = (QuestionEf) segments.get(segmentId);
			String string = (String) s.get("string");
			EAdString text = new EAdString(currentId + ".line." + id++);
			strings.put(text, string);
			question.setQuestion(text);
			Collection<StringMap<Object>> answers = (Collection<StringMap<Object>>) s
					.get("answers");
			for (StringMap<Object> a : answers) {
				EAdString line = new EAdString(currentId + ".line." + id++);
				strings.put(line, a.get("line").toString());
				EAdEffect nextEffect = segments.get(a.get("nextNode")
						.toString());
				question.addAnswer(line, nextEffect);
			}
		}
	}

	private void addLink(StringMap<Object> l) {
		try {
			String start = (String) l.get("start");
			Collection<StringMap<Object>> ends = (Collection<StringMap<Object>>) l
					.get("ends");
			EAdEffect startEffect = segments.get(start);
			if (ends.size() == 1) {
				String next = (String) ends.iterator().next().get("id");
				EAdEffect nextEffect = segments.get(next);
				startEffect.getNextEffects().add(nextEffect);
			} else {
				TriggerMacroEf triggerMacro = new TriggerMacroEf();
				for (StringMap<Object> e : ends) {
					String next = (String) e.get("id");
					StringMap<Object> cond = (StringMap<Object>) e.get("cond");
					EAdCondition condition = EmptyCond.TRUE;
					if (cond != null) {
						condition = conditionsReader.read(cond);
					}
					EAdEffect nextEffect = segments.get(next);
					triggerMacro.putEffect(nextEffect, condition);
				}
				startEffect.getNextEffects().add(triggerMacro);
			}
		} catch (Exception e) {
			logger.error("Error adding link {}", l, e);
		}
	}

	private void addSegment(StringMap<Object> s) {
		try {
			String id = (String) s.get("id");
			Collection<StringMap<Object>> nodes = (Collection<StringMap<Object>>) s
					.get("nodes");
			EAdEffect firstEffect = null;
			EAdEffect effect = null;
			for (StringMap<Object> n : nodes) {
				EAdEffect nextEffect = transform(n);
				if (firstEffect == null) {
					firstEffect = nextEffect;
				} else {
					effect.getNextEffects().add(nextEffect);
				}
				effect = nextEffect;
			}
			segments.put(id, firstEffect);
		} catch (Exception e) {
			logger.error("Exception adding segment {}", s, e);
		}
	}

	private EAdEffect transform(StringMap<Object> n) {
		EAdEffect effect = null;
		String type = (String) n.get("node");
		if (type != null) {
			if (type.equals("line")) {
				String character = (String) n.get("character");
				EAdElement sceneElement = (EAdElement) objectsFactory
						.getEAdElement(character);
				String line = (String) n.get("line");
				EAdString text = new EAdString(currentId + ".line." + id++);
				strings.put(text, line);
				SpeakSceneElementEf speak = new SpeakSceneElementEf(
						sceneElement, text);
				speak.setFont(font);
				speak.setColor(texts.get(character), bubbles.get(character));
				effect = speak;
			} else if (type.equals("effects")) {
				Collection<String> eff = (Collection<String>) n.get("effects");
				for (String ref : eff) {
					effect = (EAdEffect) objectsFactory.getEAdElement(ref);
				}
			} else if (type.equals("effect")) {
				StringMap<Object> e = (StringMap<Object>) n.get("effect");
				templateReader.applyTemplates(e);
				effect = effectsReader.read(e);
			}
		} else {
			effect = new QuestionEf();
		}
		return effect;
	}

	public void setFont(EAdFont font) {
		this.font = font;
	}

	public void addCharacter(String[] ids, EAdPaint text, EAdPaint bubble) {
		for (String id : ids) {
			texts.put(id, text);
			bubbles.put(id, bubble);
		}
	}

	public void readConversations(Collection<StringMap<Object>> conversations,
			String folder) {
		for (StringMap<Object> c : conversations) {
			read(c);
			writeStrings(folder);
		}
	}

	public void writeStrings(String folder) {
		File f = new File(folder, currentId + ".xml");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			for (Entry<EAdString, String> e : strings.entrySet()) {
				writer.write("<string name=\"" + e.getKey().toString() + "\">"
						+ e.getValue() + "</string>");
				writer.newLine();
			}
		} catch (IOException e) {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {

				}
			}
		}
	}
}
