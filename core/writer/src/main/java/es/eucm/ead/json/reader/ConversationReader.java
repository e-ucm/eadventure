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
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.text.QuestionEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.predef.effects.SpeakSceneElementEf;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.reader.model.ObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class ConversationReader {

	static private Logger logger = LoggerFactory
			.getLogger(ConversationReader.class);

	private Map<String, EAdEffect> segmentsStart;
	private Map<String, EAdEffect> segmentsEnd;
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
		segmentsStart = new HashMap<String, EAdEffect>();
		segmentsEnd = new HashMap<String, EAdEffect>();
		bubbles = new HashMap<String, EAdPaint>();
		texts = new HashMap<String, EAdPaint>();
		strings = new HashMap<EAdString, String>();
		this.objectsFactory = objectsFactory;
		this.effectsReader = effectsReader;
		this.conditionsReader = conditionsReader;
		this.templateReader = templateReader;
	}

	public EAdEffect read(StringMap<Object> conv) {
		segmentsStart.clear();
		segmentsEnd.clear();
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
		EAdEffect effect = segmentsStart.get(conv.get("start"));
		effect.setId(currentId);
		objectsFactory.putEAdElement(effect.getId(), effect);
		return effect;
	}

	private void addLinkQuestion(StringMap<Object> s) {
		try {
			StringMap<Object> n = ((Collection<StringMap<Object>>) s
					.get("nodes")).iterator().next();
			StringMap<Object> q = (StringMap<Object>) n.get("question");
			if (q != null) {
				String segmentId = (String) s.get("id");
				QuestionEf question = (QuestionEf) segmentsStart.get(segmentId);
				String string = (String) q.get("string");
				EAdString text = new EAdString(currentId + ".line." + id++);
				Boolean random = (Boolean) q.get("random");
				if (random != null) {
					question.setRandomAnswers(random.booleanValue());
				}
				strings.put(text, string);
				question.setQuestion(text);
				Collection<StringMap<Object>> answers = (Collection<StringMap<Object>>) q
						.get("answers");
				for (StringMap<Object> a : answers) {
					EAdString line = new EAdString(currentId + ".line." + id++);
					strings.put(line, a.get("line").toString());
					Collection<StringMap<Object>> effects = (Collection<StringMap<Object>>) a
							.get("effects");
					EAdEffect nextEffect = segmentsStart.get(a.get("nextNode")
							.toString());
					if (effects != null) {
						EAdEffect firstEffect = null;
						EAdEffect lastEffect = null;
						for (StringMap<Object> e : effects) {
							templateReader.applyTemplates(e);
							EAdEffect effect = effectsReader.read(e);
							if (firstEffect == null) {
								firstEffect = effect;
							} else {
								lastEffect.getNextEffects().add(effect);
							}
							lastEffect = effect;
						}
						lastEffect.getNextEffects().add(nextEffect);
						nextEffect = lastEffect;
					}
					EAdList<EAdEffect> answerEffects = new EAdList<EAdEffect>();
					answerEffects.add(nextEffect);
					question.addAnswer(line, answerEffects);
				}
			}
		} catch (Exception e) {
			logger.error("Error reading link question {}", s, e);
		}
	}

	private void addLink(StringMap<Object> l) {
		try {
			String start = (String) l.get("start");
			Collection<StringMap<Object>> ends = (Collection<StringMap<Object>>) l
					.get("ends");
			EAdEffect startEffect = segmentsEnd.get(start);
			if (ends.size() == 1) {
				String next = (String) ends.iterator().next().get("id");
				EAdEffect nextEffect = segmentsStart.get(next);
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
					EAdEffect nextEffect = segmentsStart.get(next);
					triggerMacro.putEffect(condition, nextEffect);
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
			segmentsStart.put(id, firstEffect);
			segmentsEnd.put(id, effect);
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
				speak.setNextEffectsAlways(true);
				EAdPaint bubble = bubbles.get(character);
				if (bubble == null) {
					logger.warn("No bubble paint for {}", character);
				}
				EAdPaint textP = texts.get(character);
				if (textP == null) {
					logger.warn("No text paint for {}", character);
				}
				StringMap<Object> c = (StringMap<Object>) n.get("cond");
				if (c != null) {
					speak.setCondition(conditionsReader.read(c));
				}
				speak.setColor(textP, bubble);
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
			if (text == null) {
				logger.warn("Added a null text for {}", id);
			}
			texts.put(id, text);
			if (bubble == null) {
				logger.warn("Added a null text for {}", id);
			}
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
			writer.write("<resources>");
			for (Entry<EAdString, String> e : strings.entrySet()) {
				writer.write("<string name=\"" + e.getKey().toString() + "\">"
						+ e.getValue() + "</string>");
				writer.newLine();
			}
			writer.write("</resources>");
		} catch (IOException e) {

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {

				}
			}
		}
	}
}
