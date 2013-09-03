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

package es.eucm.ead.editor.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import es.eucm.ead.editor.model.nodes.EditorNodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.inject.Inject;

import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.editor.EditorStringHandler;
import es.eucm.ead.editor.model.nodes.ActorFactory;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EditorNode;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.editor.model.nodes.StringsNode;
import es.eucm.ead.editor.model.nodes.asset.AssetFactory;
import es.eucm.ead.editor.model.nodes.asset.AssetsNode;
import es.eucm.ead.editor.model.visitor.ModelVisitor;
import es.eucm.ead.editor.model.visitor.ModelVisitorDriver;
import es.eucm.ead.importer.AdventureConverter;
import ead.importer.EAdventureImporter;
import ead.importer.annotation.ImportAnnotator;
import es.eucm.ead.reader2.AdventureReader;
import es.eucm.ead.reader.strings.StringsReader;
import es.eucm.ead.tools.PropertiesReader;
import es.eucm.ead.tools.StringHandler;
import es.eucm.ead.tools.TextFileReader;
import es.eucm.ead.editor.util.DataPrettifier;
import es.eucm.ead.tools.java.JavaTextFileReader;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLParser;
import es.eucm.ead.tools.java.utils.FileUtils;
import es.eucm.ead.writer2.AdventureWriter;
import es.eucm.ead.writer.StringWriter;

/**
 * Loads an EditorModel.
 *
 * EditorModels contain each 'Identified' element in the XML wrapped up in a
 * DependencyNode (which hosts lookup information and dependencies).
 * Non-identified elements (maps and lists) are provided transient,
 * lookup-by-value DependencyNodes, as are
 *
 * Imported editorModels are passed through a series of factories to build
 * EditorNodes. Loaded models have these EditorNodes restored from their
 * editor.xml file.
 *
 * @author mfreire
 */
public class EditorModelLoader {

	private static final Logger logger = LoggerFactory
			.getLogger(EditorModelLoader.class);

	/**
	 * Importer for old models
	 */
	private AdventureConverter converter;
	/**
	 * True only during a loading operation
	 */
	private boolean isLoading = false;
	/**
	 * Reader for DOM models
	 */
	private AdventureReader reader;
	/**
	 * Reader for zipped DOM models
	 */
	private AdventureReader zipReader;
	/**
	 * Internal zip-file reader, used to provide folder-like access to zip files
	 */
	private ZipTextFileReader zipTextFileReader;
	/**
	 * Writer for DOM models
	 */
	private AdventureWriter writer;
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
	 * Model to load or save; must be set before any operation is performed
	 */
	private EditorModelImpl model;

	@Inject
	public EditorModelLoader(XMLParser parser, ImportAnnotator annotator,
			ReflectionProvider reflectionProvider) {

		this.parser = parser;
		this.reader = new AdventureReader(reflectionProvider, parser,
				new JavaTextFileReader());
		this.zipTextFileReader = new ZipTextFileReader();
		this.zipReader = new AdventureReader(reflectionProvider, parser,
				zipTextFileReader);
		this.writer = new AdventureWriter(reflectionProvider);
		this.importAnnotator = (EditorAnnotator) annotator;
		this.saveDir = null;
		this.converter = new AdventureConverter();

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
	 * //FIXME: Folder
	 */
	public void exportGame(File target) throws IOException {
		writer.write((EAdAdventureModel) model.getEngineModel(), saveDir
				.getAbsolutePath(), new JavaTextFileWriter());
		//	target.getAbsolutePath(), ".eap",
		//				"Editor project, exported", true);
		// always adds the mappings, to allow editing it once again
		//		writeEditorNodes(target);
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
			ZipFile zip = new ZipFile(source);
			input = FileUtils.readEntryFromZip(zip, editorModelFile);
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
			DependencyNode[] changed = new DependencyNode[nodes.getLength()];
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
				changed[read++] = editorNode;
			}
			// update index
			model.fireModelEvent(new DefaultModelEvent("editor-load", this,
					null, null, changed));
		} catch (Exception e) {
			logger.error("Error reading mappings from file {}", source, e);
		}
		return read;
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
				model.addNode(null, null, target, isLoading);
				return true;
			}

			DependencyNode sourceNode = model.getNodeFor(source);
			DependencyNode e = model.addNode(sourceNode, sourceName, target,
					isLoading);

			if (e != null) {
				model.getNodeIndex().addProperty(e,
						ModelIndex.editorIdFieldName, "" + e.getId(), false);
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
	 * Builds EditorNodes from EngineNodes. Requires a 'hot' (recently updated)
	 * EditorAnnotator. Discards annotator information after use.
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
	 * @param fin
	 *            old-version file to import from
	 * @param fout
	 *            target folder to build into
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
		//		converter.addProgressListener(pp);
		converter.convert(fin.getAbsolutePath(), fout.getAbsolutePath());
		model.setEngineModel(converter.getModel());
		logger.info("{} chapters in model", model.getEngineModel()
				.getChapters().size());

		//		converter.removeProgressListener(pp);
		model.updateProgress(52, "Reading strings and engine properties ...");
		loadStringsAndProperties(fout);

		// build editor model
		logger.info("Model loaded; building graph...");
		model
				.updateProgress(55,
						"Converting engine model into editor model...");
		ModelVisitorDriver driver = new ModelVisitorDriver();
		driver.visit(model.getEngineModel(), new EditorModelVisitor(), model
				.getStringHandler());
		model.setRoot(model.getNodeFor(model.getEngineModel()));

		// add editor high-level data
		model.updateProgress(70, "Creating high-level editor elements...");
		createEditorNodes();
		writeEngineData(fout, true);
		writeEditorNodes(fout);

		// index & finish
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
		try {
			if (sourceDir.isFile()) {
				zipTextFileReader.setBase(saveDir);
				zipReader.setPath(saveDir.getAbsolutePath()
						+ File.separatorChar);
				model.setEngineModel(zipReader.readFullModel());
			} else {
				reader.setPath(saveDir.getAbsolutePath() + File.separatorChar);
				model.setEngineModel(reader.readFullModel());
			}
			model.updateProgress(52,
					"Reading strings and engine properties ...");
			loadStringsAndProperties(saveDir);
		} catch (Exception e) {
			throw new IOException("could not load from " + sourceDir, e);
		}

		// build editor model
		logger.info("Model loaded; building graph...");
		model
				.updateProgress(55,
						"Converting engine model into editor model...");
		isLoading = true;
		ModelVisitorDriver driver = new ModelVisitorDriver();
		driver.visit(model.getEngineModel(), new EditorModelVisitor(), model
				.getStringHandler());
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
			} else {
				strings = FileUtils
						.loadFileToString(new File(base, stringsFile));
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

			PropertiesReader pr = new PropertiesReader();
			HashMap<String, String> engineProperties = new HashMap<String, String>();
			// engineProperties.putAll(pr.readProperties(properties));
			logger.info("Read {} engine properties", engineProperties.size());
			model.setEngineProperties(engineProperties);
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

			// FIXME - only writes the current-language versions
			StringWriter sw = new StringWriter();
			sw.write(stringOutput, model.getStringHandler().getStrings());
			logger.info("Wrote {} strings", model.getStringHandler()
					.getStrings().size());

			if (base.isFile()) {
				ByteArrayInputStream bis;
				bis = new ByteArrayInputStream(stringOutput.toByteArray());
				FileUtils.appendEntryToZip(base, stringsFile, bis);
			} else {
				ByteArrayInputStream bis;
				bis = new ByteArrayInputStream(stringOutput.toByteArray());
				FileUtils.writeToFile(bis, new File(base, stringsFile));
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
	 * Writes the data.xml file, optionally with a human-readable copy. Also
	 * includes internationalized strings and engine properties
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
		writer.write((EAdAdventureModel) model.getEngineModel(), dest
				.getAbsolutePath(), new JavaTextFileWriter());

		if (humanReadable) {
			for (File f : dest.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.equals("manifest.xml")
							|| name.endsWith(".scene")
							|| name.endsWith(".chapter");
				}
			})) {
				DataPrettifier.prettify(f, new File(dest, "pretty-"
						+ f.getName()));
			}
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

	private static class ZipTextFileReader implements TextFileReader {
		private File zipFile;

		public void setBase(File zipFile) {
			this.zipFile = zipFile;
		}

		@Override
		public String read(String entryName) {
			try {
				return FileUtils.loadZipEntryToString(zipFile, entryName);
			} catch (IOException ioe) {
				logger.warn("Could not read file {}: {}", zipFile + "::"
						+ entryName, ioe.toString());
				return "ERROR";
			}
		}
	}
}
