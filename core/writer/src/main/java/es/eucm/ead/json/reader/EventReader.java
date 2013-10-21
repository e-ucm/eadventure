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
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.TimedEv;
import es.eucm.ead.model.elements.events.WatchFieldEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.events.enums.TimedEvType;
import es.eucm.ead.model.elements.events.enums.WatchFieldEvType;
import es.eucm.ead.model.interfaces.features.Evented;
import es.eucm.ead.reader.ObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class EventReader {

	static private Logger logger = LoggerFactory.getLogger(EventReader.class);

	private ObjectsFactory objectsFactory;
	private EffectsReader effectsReader;
	private OperationReader operationReader;
	private TemplateReader templatesReader;

	public EventReader(ObjectsFactory objectsFactory,
			EffectsReader effectsReader, OperationReader operationReader,
			TemplateReader templatesReader) {
		this.objectsFactory = objectsFactory;
		this.effectsReader = effectsReader;
		this.operationReader = operationReader;
		this.templatesReader = templatesReader;
	}

	public boolean addEvents(Collection<StringMap<Object>> events) {
		for (StringMap<Object> e : events) {
			if (e == null) {
				logger
						.warn("Null event. Make sure there is no additional commas in the file");
				return false;
			}
			Event event = null;
			String type = (String) e.get("type");
			Collection<StringMap<Object>> ef = (Collection<StringMap<Object>>) e
					.get("effects");
			List<Effect> effects = getEffects(ef);
			if (type.equals("added")) {
				event = getSceneElementEvent(SceneElementEvType.ADDED, effects);
			} else if (type.equals("removed")) {
				event = getSceneElementEvent(SceneElementEvType.REMOVED,
						effects);
			} else if (type.equals("always")) {
				event = getSceneElementEvent(SceneElementEvType.ALWAYS, effects);
			} else if (type.equals("init")) {
				event = getSceneElementEvent(SceneElementEvType.INIT, effects);
			} else if (type.equals("watchfield")) {
				event = getWatchFieldEvent(e, effects);
			} else if (type.equals("time")) {
				event = getTimeEvent(e, effects);
			}

			Collection<String> targets = (Collection<String>) e.get("targets");
			for (String target : targets) {
				Evented evented = (Evented) objectsFactory
						.getObjectById(target);
				evented.addEvent(event);
			}
		}
		return true;
	}

	private Event getTimeEvent(StringMap<Object> e, List<Effect> effects) {
		Number time = (Number) e.get("time");
		TimedEv event = new TimedEv();
		event.setTime(time.intValue());
		event.addEffects(TimedEvType.END_TIME, effects);
		event.setRepeats(1);
		return event;
	}

	private Event getWatchFieldEvent(StringMap<Object> e, List<Effect> effects) {
		WatchFieldEv event = new WatchFieldEv();
		Collection<String> fields = (Collection<String>) e.get("fields");
		for (String f : fields) {
			event.watchField(operationReader.translateField(f));
		}
		event.addEffects(WatchFieldEvType.WATCH, effects);
		return event;
	}

	public Event getSceneElementEvent(SceneElementEvType type,
			List<Effect> effects) {
		SceneElementEv event = new SceneElementEv();

		event.addEffects(type, effects);
		return event;
	}

	public List<Effect> getEffects(Collection<StringMap<Object>> ef) {
		ArrayList<Effect> effects = new ArrayList<Effect>();
		for (StringMap<Object> e : ef) {
			templatesReader.applyTemplates(e);
			Effect effect = effectsReader.read(e);
			effects.add(effect);
		}
		return effects;
	}
}
