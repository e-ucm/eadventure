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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;

import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.BasicChapter;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.reader.model.ObjectsFactory;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLParser;
import ead.writer.AdventureWriter;

public class JsonReader {

	private static final Logger logger = LoggerFactory.getLogger("JsonReader");

	private Gson gson;

	private ObjectsFactory objectsFactory;

	private AssetsReader assetsReader;

	private SceneReader sceneReader;

	private EventReader eventReader;

	private EffectsReader effectsReader;

	private OperationReader operationReader;

	private ConditionReader conditionsReader;

	private BehaviorReader behaviorReader;

	private TemplateReader templateReader;

	private AdventureWriter writer;

	private ConversationReader conversationsReader;

	public JsonReader(ReflectionProvider reflectionProvider, XMLParser xmlParser) {
		gson = new Gson();
		templateReader = new TemplateReader();
		objectsFactory = new ObjectsFactory(reflectionProvider, null);
		conditionsReader = new ConditionReader(objectsFactory);
		operationReader = new OperationReader(objectsFactory, conditionsReader);
		sceneReader = new SceneReader(objectsFactory, templateReader);
		effectsReader = new EffectsReader(objectsFactory, operationReader,
				conditionsReader, templateReader, sceneReader);
		conversationsReader = new ConversationReader(objectsFactory,
				effectsReader, conditionsReader, templateReader);
		assetsReader = new AssetsReader(objectsFactory, templateReader,
				operationReader);
		eventReader = new EventReader(objectsFactory, effectsReader,
				operationReader, templateReader);
		behaviorReader = new BehaviorReader(objectsFactory, effectsReader,
				templateReader);
		writer = new AdventureWriter(reflectionProvider, xmlParser);
	}

	public EAdAdventureModel parseGame(String[] folders) {
		BasicAdventureModel model = new BasicAdventureModel();
		model.setId("model");
		getObjectsFactory().putEAdElement("model", model);
		BasicChapter chapter = new BasicChapter();
		model.getChapters().add(chapter);

		for (String folder : folders) {
			addTemplates(folder + "/templates.json");
		}

		for (String folder : folders) {
			if (!addAssets(folder + "/assets.json")) {
				logger.error("Error parsing {}", folder + "/assets.json");
			}
		}

		for (String folder : folders) {
			EAdScene scene = getScene(folder + "/scene.json");
			if (scene != null)
				chapter.addScene(scene);
		}

		// Read effects
		for (String folder : folders) {
			addEffects(folder + "/effects.json");
		}

		// Read conversations
		for (String folder : folders) {
			addConversations(folder + "/conversations.json", folder);
		}

		for (String folder : folders) {
			addEvents(folder + "/events.json");
		}

		for (String folder : folders) {
			addBehaviors(folder + "/behaviors.json");
		}

		return model;
	}

	private void addConversations(String file, String folder) {
		Collection<StringMap<Object>> conversations = fromJson(file,
				new TypeToken<Collection<StringMap<Object>>>() {
				}.getType());
		if (conversations != null)
			conversationsReader.readConversations(conversations, folder);
	}

	private void addEffects(String file) {
		Collection<StringMap<Object>> effects = fromJson(file,
				new TypeToken<Collection<StringMap<Object>>>() {
				}.getType());
		if (effects != null)
			effectsReader.readEffects(effects);
	}

	public void write(EAdAdventureModel model, String destiny) {
		writer.write(model, destiny);
	}

	public boolean addAssets(String file) {
		Collection<StringMap<Object>> assets = fromJson(file,
				new TypeToken<Collection<StringMap<Object>>>() {
				}.getType());
		if (assets != null)
			return assetsReader.parseAssets(assets);
		return true;
	}

	public EAdScene getScene(String file) {
		StringMap<Object> scene = fromJson(file, StringMap.class);
		if (scene != null)
			return sceneReader.parseScene(scene);
		return null;
	}

	public void addEvents(String file) {
		try {
			Collection<StringMap<Object>> events = fromJson(file,
					new TypeToken<Collection<StringMap<Object>>>() {
					}.getType());
			if (events != null) {
				if (!eventReader.addEvents(events)) {
					logger
							.warn(
									"Some errors in file {}. Check previous log messages.",
									file);
				}
			}
		} catch (Exception e) {
			logger.error("Error in file {}", file, e);
		}

	}

	public void addBehaviors(String file) {
		try {
			Collection<StringMap<Object>> behaviors = fromJson(file,
					new TypeToken<Collection<StringMap<Object>>>() {
					}.getType());
			if (behaviors != null)
				behaviorReader.read(behaviors);
		} catch (Exception e) {
			logger.error("Error in file {}", file, e);
		}
	}

	public void addTemplates(String file) {
		Collection<StringMap<Object>> templates = fromJson(file,
				new TypeToken<Collection<StringMap<Object>>>() {
				}.getType());
		if (templates != null)
			for (StringMap<Object> t : templates) {
				try {
					templateReader.addTemplate(t);
				} catch (Exception e) {
					logger.error("Error in file {}: {}",
							new Object[] { file, t }, e);
				}
			}
	}

	public void registerVariable(String string, EAdVarDef<?> var) {
		objectsFactory.registerVariable(string, var);
	}

	public ObjectsFactory getObjectsFactory() {
		return objectsFactory;
	}

	public <T> T fromJson(String file, Type type) {
		File f = new File(file);
		if (f.exists()) {
			FileReader reader = null;
			try {
				reader = new FileReader(file);
				T json = gson.fromJson(reader, type);
				return json;
			} catch (Exception e) {
				logger.error("Error in file: " + file, e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {

					}
				}
			}
		}
		return null;
	}

	public ConversationReader getConversationReader() {
		return conversationsReader;
	}

}
