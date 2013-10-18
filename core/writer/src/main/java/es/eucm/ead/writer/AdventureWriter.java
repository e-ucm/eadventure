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

package es.eucm.ead.writer;

import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.model.Manifest;
import es.eucm.ead.reader.model.XMLFileNames;
import es.eucm.ead.tools.EAdUtils;
import es.eucm.ead.tools.IdGenerator;
import es.eucm.ead.tools.TextFileWriter;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLWriter;
import es.eucm.ead.writer.model.WriterVisitor;
import es.eucm.ead.writer.model.writers.ReferenceResolver;
import es.eucm.ead.writer.model.writers.SceneGraph;
import es.eucm.ead.writer.model.writers.WriterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventureWriter implements WriterContext {

	static private Logger logger = LoggerFactory
			.getLogger(AdventureWriter.class);

	private IdGenerator idGenerator;

	private List<String> contextIds;

	private XMLWriter xmlWriter;

	private WriterVisitor visitor;

	private Map<String, XMLNode> documents;

	private boolean enableSimplifications;

	private boolean enableTranslations = true;

	private ReferenceResolver referenceResolver;

	private SceneGraph sceneGraph;

	private int contextId;

	private EAdMap<String, String> paramsTranslation;
	private EAdMap<String, String> fieldsTranslation;
	private EAdMap<String, String> classesTranslation;

	public AdventureWriter(ReflectionProvider reflectionProvider) {
		idGenerator = new IdGenerator();
		xmlWriter = new XMLWriter();
		referenceResolver = new ReferenceResolver();
		sceneGraph = new SceneGraph();
		visitor = new WriterVisitor(reflectionProvider, this);
		documents = new HashMap<String, XMLNode>();
		contextIds = new ArrayList<String>();
		paramsTranslation = new EAdMap<String, String>();
		fieldsTranslation = new EAdMap<String, String>();
		classesTranslation = new EAdMap<String, String>();
	}

	/**
	 * Sets if the writer must enable translations for classes, maps and params.
	 * This reduces the size of model files, but uglyfies the XML. It's set to true by default
	 *
	 * @param enableTranslations whether classes, maps and params translations is enabled
	 */
	public void setEnableTranslations(boolean enableTranslations) {
		this.enableTranslations = enableTranslations;
	}

	/**
	 * Generates the xml documents necessaries to represent the given model.
	 *
	 * @param model the game model
	 */
	public void write(AdventureGame model, String path,
			TextFileWriter textFileWriter) {
		idGenerator.clear();
		contextIds.clear();
		visitor.clear();
		documents.clear();
		referenceResolver.clear();
		sceneGraph.clear();
		contextId = 0;
		paramsTranslation.clear();
		fieldsTranslation.clear();
		classesTranslation.clear();

		AdventureVisitorListener chapterListener = new AdventureVisitorListener(
				XMLFileNames.CHAPTER);
		AdventureVisitorListener sceneListener = new AdventureVisitorListener(
				XMLFileNames.SCENE);

		ChangeContextListener changeContext = new ChangeContextListener(null);
		// Write chapters
		for (Chapter c : model.getChapters()) {
			visitor.writeElement(null, null, changeContext);
			visitor.writeElement(c, null, chapterListener);
			visitor.finish();
			ChangeContextListener changeContextScene = new ChangeContextListener(
					c.getId());
			for (Scene s : c.getScenes()) {
				visitor.writeElement(null, null, changeContextScene);
				visitor.writeElement(s, null, sceneListener);
				visitor.finish();
			}
		}

		Manifest manifest = generateManifest(model);
		manifest.setSceneGraph(sceneGraph.getGraph());

		// Write manifest.xml
		boolean oldEnableTranslations = this.enableTranslations;
		// The manifest is written without translations
		this.enableTranslations = false;
		visitor.writeElement(null, null, changeContext);
		visitor.writeElement(manifest, null,
				new WriterVisitor.VisitorListener() {
					@Override
					public void load(XMLNode node, Object object) {
						documents.put(XMLFileNames.MANIFEST, node);
					}
				});
		visitor.finish();
		this.enableTranslations = oldEnableTranslations;

		// Resolve reference
		referenceResolver.resolveReferences();

		if (!path.endsWith("/")) {
			path += "/";
		}
		for (Map.Entry<String, XMLNode> e : documents.entrySet()) {
			textFileWriter.write(xmlWriter.generateString(e.getValue()), path
					+ e.getKey());
		}
	}

	/**
	 * Generate the manifest for the given model
	 *
	 * @param model
	 * @return
	 */
	public Manifest generateManifest(AdventureGame model) {
		Manifest manifest = new Manifest();
		// We need to clear to initialize the manifest
		manifest.clear();

		manifest.setId(this.generateNewId());
		manifest.setClasses(EAdUtils.invertMap(classesTranslation));
		manifest.setFields(EAdUtils.invertMap(fieldsTranslation));
		manifest.setParams(EAdUtils.invertMap(paramsTranslation));

		// Set model
		manifest.setModel(model);
		// Chapters list
		for (Chapter chapter : model.getChapters()) {
			manifest.addChapterId(chapter.getId());
			// Initial list
			Scene scene = chapter.getInitialScene();
			if (scene == null) {
				logger.warn("Chapter {} has no initial scene.");
				if (chapter.getScenes().size() > 0) {
					logger.warn("Using first scene in the list.");
					scene = chapter.getScenes().get(0);
				} else {
					logger
							.warn(
									"Chapter {} has no scenes. That is a problem. An empty scene has been generated.",
									chapter.getId());
					scene = new Scene();
					scene
							.setId(idGenerator
									.generateNewId("placeholder_scene_"));
				}
			}
			manifest.addInitScene(scene.getId());
			for (Scene s : chapter.getScenes()) {
				manifest.addScene(chapter.getId(), s.getId());
			}
		}

		// Initial chapter
		Chapter initialChapter = model.getInitialChapter();
		if (initialChapter == null) {
			if (model.getChapters().size() > 0) {
				logger
						.warn("This game has no initial chapter. Using first chapter in the list by default");
				initialChapter = model.getChapters().get(0);
			} else {
				logger.warn("This game has no chapters.");
			}
		}

		if (initialChapter != null) {
			manifest.setInitialChapter(initialChapter.getId());
		}

		return manifest;
	}

	@Override
	public int getContextId() {
		return contextId;
	}

	@Override
	public String generateNewId() {
		return idGenerator.generateNewId("");
	}

	@Override
	public boolean containsId(String id) {
		return contextIds.contains(id);
	}

	@Override
	public String translateClass(Class<?> type) {
		if (enableTranslations) {
			return getTranslation(type.getName(), classesTranslation);
		}
		return type.getName();
	}

	private String getTranslation(String string,
			EAdMap<String, String> mapTranslations) {
		String value = mapTranslations.get(string);
		if (value == null) {
			value = EAdUtils.generateId("", mapTranslations.size());
			mapTranslations.put(string, value);
		}
		return value;
	}

	@Override
	public String translateField(String name) {
		if (enableTranslations) {
			return getTranslation(name, fieldsTranslation);
		}
		return name;
	}

	@Override
	public String translateParam(String param) {
		if (enableTranslations) {
			return getTranslation(param, paramsTranslation);
		}
		return param;
	}

	@Override
	public Object process(Object object, XMLNode node) {

		if (object instanceof Identified) {
			referenceResolver.check((Identified) object, node, this);
			contextIds.add(((Identified) object).getId());
			idGenerator.addExclusion(((Identified) object).getId());
		}

		sceneGraph.process(object);

		return object;
	}

	public void setEnableSimplifications(boolean enableSimplifications) {
		this.enableSimplifications = enableSimplifications;
	}

	public class AdventureVisitorListener implements
			WriterVisitor.VisitorListener {

		private String extension;

		public AdventureVisitorListener(String extension) {
			this.extension = extension;
		}

		@Override
		public void load(XMLNode node, Object object) {
			documents
					.put(((Identified) object).getId() + "." + extension, node);
		}
	}

	public class ChangeContextListener implements WriterVisitor.VisitorListener {

		private String chapterId;

		public ChangeContextListener(String chapterId) {
			this.chapterId = chapterId;
		}

		@Override
		public void load(XMLNode node, Object object) {
			contextId++;
			contextIds.clear();
			// We add the chapter id to the context to avoid de regeneration of
			// the chapter XML inside the scenes. Instead, a reference will be used.
			// The chapter will be available during reader, since chapter all always
			// reade before scenes.
			if (chapterId != null) {
				contextIds.add(chapterId);
			}
		}
	}

}
