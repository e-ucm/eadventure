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

package ead.editor.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.inject.Inject;

import ead.common.interfaces.features.Identified;
import ead.common.model.elements.EAdAdventureModel;
import ead.editor.EditorStringHandler;
import ead.editor.model.nodes.ActorFactory;
import ead.editor.model.nodes.DependencyEdge;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EditorNode;
import ead.editor.model.nodes.EditorNodeFactory;
import ead.editor.model.nodes.EngineNode;
import ead.editor.model.nodes.StringsNode;
import ead.editor.model.nodes.asset.AssetFactory;
import ead.editor.model.nodes.asset.AssetsNode;
import ead.editor.model.visitor.ModelVisitor;
import ead.editor.model.visitor.ModelVisitorDriver;
import ead.importer.EAdventureImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.reader.adventure.AdventureReader;
import ead.reader.strings.StringsReader;
import ead.tools.StringHandler;
import ead.tools.xml.XMLParser;
import ead.utils.FileUtils;
import ead.writer.DataPrettifier;
import ead.writer.EAdAdventureModelWriter;
import ead.writer.StringWriter;

/**
 * Loads an EditorModel.
 *
 * EditorModels contain each 'Identified' element in the XML wrapped up in a
 * DependencyNode (which hosts lookup information and dependencies). Non-identified
 * elements (maps and lists) are provided transient, lookup-by-value DependencyNodes,
 * as are
 *
 * Imported editorModels are passed through a series of factories to build
 * EditorNodes. Loaded models have these EditorNodes restored from their
 * editor.xml file.
 *
 * @author mfreire
 */
public class EditorModelLoader {

	private static final Logger logger = LoggerFactory
			.getLogger("EditorModelLoader");

	/**
	 * Importer for old models
	 */
	private EAdventureImporter importer;
	/**
	 * True only during a loading operation
	 */
	private boolean isLoading = false;
	/**
	 * Reader for DOM models
	 */
	private AdventureReader reader;
	/**
	 * Writer for DOM models
	 */
	private EAdAdventureModelWriter writer;
	/**
	 * Parser for XML documents
	 */
	private XMLParser parser;
	/**
	 * Current project directory; used to save & load
	 */
	private File saveDir;
	/**
	 * An import annotator that can reconstitute a bit of an existing import
	 */
	private EditorAnnotator importAnnotator;

	/**
	 * A list of editor node factories for imports
	 */
	private ArrayList<EditorNodeFactory> importNodeFactories = new ArrayList<EditorNodeFactory>();
	/**
	 * Name of file with editor-node descriptions
	 */
	private static final String editorModelFile = "editor.xml";
	/**
	 * Name of file with strings
	 */
	private static final String stringsFile = "strings.xml";
	/**
	 * Name of file with engine properties
	 */
	private static final String enginePropertiesFile = "ead.properties";
	/**
	 * Name of all-important data-file with engine properties
	 */
	private static final String engineModelFile = "data.xml";

	/**
	 * Model to load or save; must be set before any operation is performed
	 */
	private EditorModelImpl model;

	@Inject
	public EditorModelLoader(XMLParser parser, EAdventureImporter importer,
			EAdAdventureModelWriter writer, ImportAnnotator annotator) {

		this.parser = parser;
		this.reader = new AdventureReader(parser);
		this.importer = importer;
		this.writer = writer;
		this.importAnnotator = (EditorAnnotator) annotator;
		this.saveDir = null;

		importNodeFactories.add(new ActorFactory());
		importNodeFactories.add(new AssetFactory());
	}

	public void setModel(EditorModelImpl model) {
		this.model = model;
	}

	/**
	 * Exports the editor model into a zip file.
	 *
	 * @param target
	 *            ; if null, previous target is assumed
	 * @throws IOException
	 */
	public void exportGame(File target) throws IOException {
		importer.createGameFile((EAdAdventureModel) model.getEngineModel(),
				saveDir.getAbsolutePath(), target.getAbsolutePath(), ".eap",
				"Editor project, exported", true);
		// always adds the mappings, to allow editing it once again
		writeEditorNodes(target);
	}

	/**
	 * Writes the editor mappings to an editor.xml file.
	 *
	 * @param dest
	 * @return number of mappings written
	 */
	private int writeEditorNodes(File target) throws IOException {
		int mappings = 0;
		StringBuilder sb = new StringBuilder("<editorNodes>\n");
		for (DependencyNode n : model.getNodesById().values()) {
			if (n instanceof EditorNode) {
				logger.debug("Writing editorNode of type {} with id {}",
						new Object[] { n.getClass(), n.getId() });
				((EditorNode) n).write(sb);
				mappings++;
			}
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(sb.append(
				"</editorNodes>\n").toString().getBytes("UTF-8"));
		OutputStream fos;

		if (target.isFile()) {
			FileUtils.appendEntryToZip(target, editorModelFile, bis);
		} else {
			FileUtils.writeToFile(bis, new File(target, editorModelFile));
		}

		return mappings;
	}

	/**
	 * Reads the editor mappings from an editor.xml file.
	 *
	 * @param source
	 * @return number of mappings read
	 */
	private int readEditorNodes(File source) throws IOException {
		InputStream input;
		if (source.isFile()) {
			input = FileUtils.readEntryFromZip(source, editorModelFile);
		} else {
			input = new BufferedInputStream(new FileInputStream(new File(
					source, editorModelFile)));
		}

		int read = 0;
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(input);
			ClassLoader cl = this.getClass().getClassLoader();
			NodeList nodes = doc.getElementsByTagName("node");
			logger.debug("Parsed {} fine; {} mappings read OK", new Object[] {
					source, nodes.getLength() });
			// build
			for (int i = 0; i < nodes.getLength(); i++) {
				Element e = (Element) nodes.item(i);
				String className = e.getAttribute("class");
				int id = Integer.valueOf(e.getAttribute("id"));
				logger.debug("\trestoring {} {}",
						new Object[] { className, id });
				EditorNode editorNode = (EditorNode) cl.loadClass(className)
						.getConstructor(Integer.TYPE).newInstance(id);
				model.getNodesById().put(id, editorNode);
			}
			// initialize
			for (int i = 0; i < nodes.getLength(); i++) {
				Element e = (Element) nodes.item(i);
				int id = Integer.valueOf(e.getAttribute("id"));
				EditorNode editorNode = (EditorNode) model.getNodesById().get(
						id);
				String childrenIds = e.getAttribute("contents");
				logger.debug("\tinitializing {}, {}", new Object[] { id,
						childrenIds });
				for (String idString : childrenIds.split("[,]")) {
					if (idString.isEmpty()) {
						// this can happen if there are no values...
						continue;
					}
					int cid = Integer.valueOf(idString);
					logger.debug("\tadding child {}", cid);
					if (model.getNodesById().get(cid) == null) {
						logger
								.error(
										"Cannot add child {} of editorNode {}: null child (id {} not registered)",
										new Object[] { idString, id, idString });
					} else {
						editorNode.addChild(model.getNodesById().get(cid));
						logger.debug("\tadding child {} [{}]", new Object[] {
								cid,
								model.getNodesById().get(cid)
										.getTextualDescription(model) });
					}
				}
				editorNode.restoreInner(e, model);
				model.registerEditorNodeWithGraph(editorNode);
				read++;
			}
		} catch (Exception e) {
			logger.error("Error reading mappings from file {}", source, e);
		}
		return read;
	}

	/**
	 * Wraps a targetContent in an DependencyNode. If the content is of a type
	 * that has extra editor data associated (a subclass of EAdElement), and
	 * this editor data is available, it is used; otherwise, a new
	 * DependencyNode is created.
	 *
	 * @param targetContent
	 *            to wrap
	 * @return a new or old editorNode to wrap that content
	 */
	@SuppressWarnings("unchecked")
	private DependencyNode createOrUnfreezeNode(Object targetContent) {

		DependencyNode node;
		if (targetContent instanceof Identified) {
			String oid = ((Identified) targetContent).getId();
			int eid = model.getEditorId(targetContent);
			if (eid != EditorModelImpl.badElementId) {
				// content is eadElement, and has editor-id: unfreeze
				logger.debug("Found existing eID marker in {}: {}",
						targetContent.getClass().getSimpleName(), oid);
				node = model.getNodesById().get(eid);
				if (node == null) {
					node = new EngineNode(eid, targetContent);
					model.getNodesById().put(eid, node);
				} else {
					if (!node.getContent().equals(targetContent)) {
						logger
								.error(
										"Corrupted save-file: eid {} assigned to {} AND {}",
										new Object[] { eid,
												targetContent.toString(),
												node.getContent().toString() });
						throw new IllegalStateException("Corrupted save-file: "
								+ "same eid assigned to two objects");
					}
				}
			} else {
				// content is eadElement, but has no editor-id: add it
				if (isLoading) {
					logger.error(
							"Loaded EAdElement {} of type {} had no editor ID",
							new String[] { oid,
									targetContent.getClass().getSimpleName() });
					throw new IllegalStateException("Corrupted save-file: "
							+ "no eid assigned to loaded objects");
				} else {
					eid = model.generateId(targetContent);
					String decorated = EditorModelImpl.decorateIdWithEid(oid,
							eid);
					logger.debug("Created eID marker for {}: {} ({})",
							new Object[] { oid, eid, decorated });
					((Identified) targetContent).setId(decorated);
					node = new EngineNode(eid, targetContent);
					model.getNodesById().put(eid, node);
				}
			}
		} else {
			// content cannot have true editor-id at all (it is transient)
			int eid = model.generateId(targetContent);
			logger.debug("Created eID for non-persited {}: {}", new Object[] {
					targetContent.getClass().getSimpleName(), eid });
			node = new EngineNode(eid, targetContent);
			model.getNodesById().put(eid, node);
			model.getNodesByContent().put(targetContent, node);
		}
		return node;
	}

	/**
	 * Attempts to add a new node-and-edge to the graph; use only during initial
	 * model-building. The source may be null (for the root).
	 *
	 * @return the new node if added, or null if already existing (and
	 *         therefore, it makes no sense to continue adding recursively from
	 *         there on).
	 */
	private DependencyNode addNode(DependencyNode source, String type,
			Object targetContent) {
		DependencyNode target = model.getNodeFor(targetContent);
		boolean alreadyKnown = (target != null);

		if (!alreadyKnown) {
			target = createOrUnfreezeNode(targetContent);
			model.getGraph().addVertex(target);
		}

		if (source != null) {
			model.getGraph().addEdge(source, target, new DependencyEdge(type));
		} else {
			model.setRoot(target);
		}

		if (!alreadyKnown) {
			return target;
		} else {
			return null;
		}
	}

	private class EditorModelVisitor implements ModelVisitor {
		/**
		 * Visits a node
		 *
		 * @see ModelVisitor#visitObject
		 */
		@Override
		public boolean visitObject(Object target, Object source,
				String sourceName) {
			logger.debug("Visiting object: '{}'--['{}']-->'{}'", new Object[] {
					source, sourceName, target });

			// source is only null for root node
			if (source == null) {
				// should keep on drilling, but otherwise nothing to do here
				addNode(null, null, target);
				return true;
			}

			DependencyNode sourceNode = model.getNodeFor(source);
			DependencyNode e = addNode(sourceNode, sourceName, target);

			if (e != null) {
				model.getNodeIndex().addProperty(e,
						ModelIndex.editorIdFieldName, "" + e.getId(), true);
				return true;
			} else {
				// already exists in graph; in this case, do not drill deeper
				return false;
			}
		}

		/**
		 * Visits a node property. Mostly used for indexing
		 *
		 * @see ModelVisitor#visitProperty
		 */
		@Override
		public void visitProperty(Object target, String propertyName,
				String textValue) {
			logger.debug("Visiting property: '{}' :: '{}' = '{}'",
					new Object[] { target, propertyName, textValue });
			DependencyNode targetNode = model.getNodeFor(target);
			model.getNodeIndex().addProperty(targetNode, propertyName,
					textValue, true);
		}
	}

	/**
	 * Builds EditorNodes from EngineNodes.
	 * Requires a 'hot' (recently updated) EditorAnnotator. Discards
	 * annotator information after use.
	 */
	private void createEditorNodes() {
		// engine ids may have changed during load
		importAnnotator.rebuild();

		for (EditorNodeFactory enf : importNodeFactories) {
			enf.createNodes(model, importAnnotator);
		}

		model
				.registerEditorNodeWithGraph(new AssetsNode(model
						.generateId(null)));
		model.registerEditorNodeWithGraph(new StringsNode(model
				.generateId(null)));

		importAnnotator.reset();
	}

	// ----- Import, Load, Save
	/**
	 * Loads data from an EAdventure1.x game file. Saves this as an EAdventure
	 * 2.x editor file.
	 *
	 * @param fin old-version file to import from
	 * @param fout target folder to build into
	 */
	public void loadFromImportFile(File fin, File fout) throws IOException {
		logger.info(
				"Loading editor model from EAD 1.x import '{}' into '{}'...",
				fin, fout);

		// clear caches & start timer
		clear();
		importAnnotator.reset();
		long nanos = System.nanoTime();

		// start import
		saveDir = fout;
		ProgressProxy pp = new ProgressProxy(0, 0.5f);
		model.updateProgress(0, "Starting import...");
		importer.addProgressListener(pp);
		model.setEngineModel(importer.importGame(fin.getAbsolutePath(), fout
				.getAbsolutePath()));
		importer.removeProgressListener(pp);
		model.updateProgress(52, "Reading strings and engine properties ...");
		loadStringsAndProperties(fout);

		// build editor model
		logger.info("Model loaded; building graph...");
		model
				.updateProgress(55,
						"Converting engine model into editor model...");
		ModelVisitorDriver driver = new ModelVisitorDriver();
		driver.visit(model.getEngineModel(), new EditorModelVisitor(), model.getStringHandler());
		model.setRoot(model.getNodeFor(model.getEngineModel()));

		// add editor high-level data
		model.updateProgress(70, "Creating high-level editor elements...");
		createEditorNodes();
		writeEngineData(fout, true);
		writeEditorNodes(fout);

		// index  & finish
		model.updateProgress(90, "Indexing model ...");
		model.getNodeIndex().firstIndexUpdate(model.getGraph().vertexSet());
		model.updateProgress(100, "... load complete.");

		logger.info("Editor model loaded: {} nodes, {} edges, {} seconds",
				new Object[] { model.getGraph().vertexSet().size(),
						model.getGraph().edgeSet().size(), time(nanos) });
	}

	/**
	 * Loads the editor model. Discards the current editing session. The file
	 * must have been built with save(). Any presentation-related data should be
	 * added after this is called, using FileUtils.readEntryFromZip(source, ...)
	 *
	 * @param sourceDir
	 * @throws IOException
	 */
	public void load(File sourceDir) throws IOException {
		logger.info("Loading editor model from project dir '{}'...", sourceDir);

		// clear caches & start timer
		clear();
		long nanos = System.nanoTime();

		// read
		saveDir = sourceDir;
		model.updateProgress(10, "Reading engine model ...");
		String xml;
		if (sourceDir.isFile()) {
			xml = FileUtils.loadZipEntryToString(saveDir, engineModelFile);
		} else {
			xml = FileUtils
					.loadFileToString(new File(saveDir, engineModelFile));
		}
		model.setEngineModel(reader.readXML(xml));
		model.updateProgress(52, "Reading strings and engine properties ...");
		loadStringsAndProperties(saveDir);

		// build editor model
		logger.info("Model loaded; building graph...");
		model
				.updateProgress(55,
						"Converting engine model into editor model...");
		isLoading = true;
		ModelVisitorDriver driver = new ModelVisitorDriver();
		driver.visit(model.getEngineModel(), new EditorModelVisitor(), model.getStringHandler());
		isLoading = false;
		bumpLastElementNodeId();
		model.setRoot(model.getNodeFor(model.getEngineModel()));

		// index
		model.updateProgress(70, "Indexing model ...");
		model.getNodeIndex().firstIndexUpdate(model.getGraph().vertexSet());
		model.updateProgress(80, "... load complete.");

		// add editor high-level data & finish
		model.updateProgress(70, "Creating high-level editor elements...");
		readEditorNodes(sourceDir);
		bumpLastElementNodeId();

		logger.info("Editor model loaded: {} nodes, {} edges, {} seconds",
				new Object[] { model.getGraph().vertexSet().size(),
						model.getGraph().edgeSet().size(), time(nanos) });
	}

	private void bumpLastElementNodeId() {
		// set next editor-id to higher than current highest
		int highestAssigned = model.getNodesById().floorKey(
				EditorModelImpl.intermediateIDPoint - 1);
		logger.debug("Bumping lastElementId to closest to {}: {}",
				new Object[] { EditorModelImpl.intermediateIDPoint - 1,
						highestAssigned + 1 });
		model.setLastElementNodeId(highestAssigned + 1);
	}

	/**
	 * load strings and properties
	 */
	private void loadStringsAndProperties(File base) {
		try {
			String strings, properties;
			if (base.isFile()) {
				strings = FileUtils.loadZipEntryToString(base, stringsFile);
				properties = FileUtils.loadZipEntryToString(base,
						enginePropertiesFile);
			} else {
				strings = FileUtils
						.loadFileToString(new File(base, stringsFile));
				properties = FileUtils.loadFileToString(new File(base,
						enginePropertiesFile));
			}

			// FIXME - only reads the current-language versions
			StringsReader sr = new StringsReader(parser);
			EditorStringHandler stringHandler = new EditorStringHandler();
			stringHandler.setStrings(sr.readStrings(strings));
			logger.info("Read {} strings", stringHandler.getStrings().size());
			model.setStringHandler(stringHandler);
			// stringNode retrievable via m.getNodeFor(m.getStringHandler());
			DependencyNode stringsNode = new EngineNode<StringHandler>(model
					.generateId(null), stringHandler);
			model.getNodesByContent().put(stringHandler, stringsNode);
		} catch (Exception e) {
			logger.error("Could not load strings or properties", e);
		}
	}

	/**
	 * save strings and properties
	 */
	private void saveStringsAndProperties(File base) {
		try {
			ByteArrayOutputStream stringOutput = new ByteArrayOutputStream();
			ByteArrayOutputStream propertiesOutput = new ByteArrayOutputStream();

			// FIXME - only writes the current-language versions
			StringWriter sw = new StringWriter();
			sw.write(stringOutput, model.getStringHandler().getStrings());
			logger.info("Wrote {} strings", model.getStringHandler()
					.getStrings().size());

			Properties p = new Properties();
			p.putAll(model.getEngineProperties());
			p.store(propertiesOutput, "Saved from editor on "
					+ (new GregorianCalendar()));

			if (base.isFile()) {
				ByteArrayInputStream bis;
				bis = new ByteArrayInputStream(stringOutput.toByteArray());
				FileUtils.appendEntryToZip(base, stringsFile, bis);
				bis = new ByteArrayInputStream(propertiesOutput.toByteArray());
				FileUtils.appendEntryToZip(base, enginePropertiesFile, bis);
			} else {
				ByteArrayInputStream bis;
				bis = new ByteArrayInputStream(stringOutput.toByteArray());
				FileUtils.writeToFile(bis, new File(base, stringsFile));
				bis = new ByteArrayInputStream(propertiesOutput.toByteArray());
				FileUtils
						.writeToFile(bis, new File(base, enginePropertiesFile));
			}

			logger.info("Wrote {} engine properties", model
					.getEngineProperties().size());
		} catch (Exception e) {
			logger.error("Could not write strings or properties", e);
		}
	}

	/**
	 * Saves the editor model. Save will contain a normal EAdModel, plus
	 * resources, plus editor-specific model nodes. Does not include anything
	 * presentation- related; that should be appended via
	 * FileUtils.appendEntryToZip(target, ...)
	 *
	 * @param target
	 *            ; if null, previous target is assumed
	 * @throws IOException
	 */
	public void save(File target) throws IOException {

		long nanos = System.nanoTime();

		model.updateProgress(5, "Commencing save ...");
		if (target != null && saveDir != target) {
			// copy over all resource-files first
			model
					.updateProgress(10,
							"Copying resources to new destination ...");
			// works for zip-files as well as for whole folders
			FileUtils.copy(saveDir, target);
		} else if (target == null && saveDir != null) {
			target = saveDir;
		} else {
			throw new IllegalArgumentException(
					"Cannot save() without knowing where!");
		}

		// write main xml
		model.updateProgress(50, "Writing engine model ...");
		writeEngineData(target, logger.isDebugEnabled());

		// write extra xml file to it
		model.updateProgress(80, "Writing editor model ...");
		int mappings = 0;
		try {
			mappings = writeEditorNodes(target);
		} catch (IOException ioe) {
			logger.error("Could not write editor.xml file to {}", target, ioe);
		}

		saveDir = target;
		model.updateProgress(100, "... save complete.");

		logger.info("Wrote editor data from {} to {}: {} total objects,"
				+ " {} editor mappings, in {} seconds", new Object[] { saveDir,
				target, model.getNodesById().size(), mappings, time(nanos) });
	}

	/**
	 * Shows how many seconds an operation takes
	 */
	private String time(long nanoStart) {
		long t = System.nanoTime() - nanoStart;
		DecimalFormat df = new DecimalFormat("#,###.000");
		return df.format((t / 1000000L) / 1000.0);
	}

	/**
	 * Clears all model information
	 */
	private void clear() {
		model.clear();
		isLoading = false;
		importAnnotator.reset();
	}

	/**
	 * Returns a file that is relative to this save-file
	 *
	 * @param name
	 *            of file to return, relative to save-file
	 */
	public File relativeFile(String name) {
		if (saveDir.exists() && saveDir.isDirectory()) {
			return new File(saveDir, name);
		} else {
			throw new IllegalArgumentException(
					"Nothing loaded, loadRelative not available");
		}
	}

	/**
	 * Writes the data.xml file, optionally with a human-readable copy.
	 * Also includes internationalized strings and engine properties
	 *
	 * @param dest
	 *            destination file
	 * @param humanReadable
	 *            whether to create a readable copy
	 * @return
	 */
	private void writeEngineData(File dest, boolean humanReadable)
			throws IOException {

		// data
		File destFile = new File(dest, "data.xml");
		OutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
			writer.write((EAdAdventureModel) model.getEngineModel(), out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		if (humanReadable) {
			DataPrettifier.prettify(destFile, new File(dest, "pretty-"
					+ destFile.getName()));
		}

		// strings and props
		saveStringsAndProperties(dest);
	}

	public File getSaveDir() {
		return saveDir;
	}

	/**
	 * Re-issues importer progress updates as own updates
	 */
	public class ProgressProxy implements
			EAdventureImporter.ImporterProgressListener {

		private int start;
		private float factor;

		public ProgressProxy(int start, float factor) {
			this.start = start;
			this.factor = factor;
		}

		@Override
		public void update(int progress, String text) {
			model.updateProgress(start + (int) (progress * factor), text);
		}
	}
}
