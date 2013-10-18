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
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.interfaces.features.WithBehavior;
import es.eucm.ead.model.params.guievents.EAdGUIEvent;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.reader.ObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class BehaviorReader {

	static private Logger logger = LoggerFactory
			.getLogger(BehaviorReader.class);

	private ObjectsFactory objectsFactory;

	private EffectsReader effectsReader;

	private TemplateReader templateReader;

	public BehaviorReader(ObjectsFactory objectsFactory,
			EffectsReader effectsReader, TemplateReader templateReader) {
		super();
		this.objectsFactory = objectsFactory;
		this.effectsReader = effectsReader;
		this.templateReader = templateReader;
	}

	public void read(Collection<StringMap<Object>> bs) {
		for (StringMap<Object> b : bs) {
			templateReader.applyTemplates(b);
			String id = b.get("target").toString();
			WithBehavior element = (WithBehavior) objectsFactory
					.getObjectById(id);
			if (element == null) {
				logger.warn("No element for id {}", id);
			}
			EAdGUIEvent event = getGUIEvent(b.get("type").toString());
			Collection<StringMap<Object>> effects = (Collection<StringMap<Object>>) b
					.get("effects");
			for (StringMap<Object> s : effects) {
				EAdEffect effect = effectsReader.read(s);
				element.addBehavior(event, effect);
			}
		}
	}

	private EAdGUIEvent getGUIEvent(String type) {
		if (type.equals("press")) {
			return MouseGEv.MOUSE_LEFT_PRESSED;
		}
		return MouseGEv.MOUSE_LEFT_PRESSED;
	}

}
