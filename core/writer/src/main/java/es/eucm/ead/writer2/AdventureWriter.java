package es.eucm.ead.writer2;

import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.scenes.BasicScene;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader2.model.Manifest;
import es.eucm.ead.reader2.model.XMLFileNames;
import es.eucm.ead.tools.TextFileWriter;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLWriter;
import es.eucm.ead.writer2.model.ReferenceResolver;
import es.eucm.ead.writer2.model.WriterContext;
import es.eucm.ead.writer2.model.WriterVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventureWriter implements WriterContext {

	private static final Logger logger = LoggerFactory
			.getLogger("AdventureWriter");

	private IdGenerator idGenerator;

	private List<String> contextIds;

	private XMLWriter xmlWriter;

	private WriterVisitor visitor;

	private Map<String, XMLNode> documents;

	private boolean enableSimplifications;

	private ReferenceResolver referenceResolver;

	private int contextId;

	public AdventureWriter(ReflectionProvider reflectionProvider) {
		idGenerator = new IdGenerator();
		xmlWriter = new XMLWriter();
		referenceResolver = new ReferenceResolver();
		visitor = new WriterVisitor(reflectionProvider, this);
		documents = new HashMap<String, XMLNode>();
		contextIds = new ArrayList<String>();
	}

	/**
	 * Generates the xml documents necessaries to represent the given model.
	 *
	 * @param model the game model
	 * @return a map with all the xml documents to represent the game. The key is a string: the name of the file to store its associated xml
	 */
	public void write(EAdAdventureModel model, String path,
			TextFileWriter textFileWriter) {
		documents.clear();

		AdventureVisitorListener chapterListener = new AdventureVisitorListener(
				XMLFileNames.CHAPTER);
		AdventureVisitorListener sceneListener = new AdventureVisitorListener(
				XMLFileNames.SCENE);
		WriterVisitor.VisitorListener changeContext = new WriterVisitor.VisitorListener() {
			@Override
			public void load(XMLNode node, Object object) {
				contextId++;
				contextIds.clear();
			}
		};
		// Write chapters
		for (EAdChapter c : model.getChapters()) {
			visitor.writeElement(null, null, changeContext);
			visitor.writeElement(c, null, chapterListener);
			visitor.finish();
			for (EAdScene s : c.getScenes()) {
				visitor.writeElement(null, null, changeContext);
				visitor.writeElement(s, null, sceneListener);
				visitor.finish();
			}
		}

		Manifest manifest = generateManifest(model);

		// Write manifest.xml
		visitor.writeElement(null, null, changeContext);
		visitor.writeElement(manifest, null,
				new WriterVisitor.VisitorListener() {
					@Override
					public void load(XMLNode node, Object object) {
						documents.put(XMLFileNames.MANIFEST, node);
					}
				});
		visitor.finish();

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
	public Manifest generateManifest(EAdAdventureModel model) {
		Manifest manifest = new Manifest();
		// We need to clear to initialize the manifest
		manifest.clear();

		manifest.setId(this.generateNewId());

		// Set model
		manifest.setModel(model);
		// Chapters list
		for (EAdChapter chapter : model.getChapters()) {
			manifest.addChapterId(chapter.getId());
			// Initial list
			EAdScene scene = chapter.getInitialScene();
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
					scene = new BasicScene();
					scene
							.setId(idGenerator
									.generateNewId("placeholder_scene_"));
				}
			}
			manifest.addInitScene(scene.getId());
		}

		// Initial chapter
		EAdChapter initialChapter = model.getInitialChapter();
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
		return type.getName();
	}

	@Override
	public String translateField(String name) {
		return name;
	}

	@Override
	public String translateParam(String param) {
		return param;
	}

	@Override
	public Object process(Object object, XMLNode node) {

		if (object instanceof Identified) {
			referenceResolver.check((Identified) object, node, this);
			contextIds.add(((Identified) object).getId());
		}

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

}
