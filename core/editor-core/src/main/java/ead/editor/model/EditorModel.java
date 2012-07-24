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

import com.google.inject.Inject;
import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.editor.model.visitor.ModelVisitor;
import ead.editor.model.visitor.ModelVisitorDriver;
import ead.editor.view.dock.ModelAccessor;
import ead.importer.EAdventure1XImporter;
import ead.reader.java.EAdAdventureDOMModelReader;
import ead.reader.java.ProjectFiles;
import ead.utils.FileUtils;
import ead.writer.EAdAdventureModelWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains a full model of what is being edited. This is a super-set of an
 * EAdAdventureModel, encompassing both engine-related model objects and
 * resources, assets, and strings. Everything is searchable, and dependencies
 * are tracked as objects are changed.
 *
 * @author mfreire
 */
public class EditorModel implements ModelVisitor, ModelAccessor {

    private static final Logger logger = LoggerFactory.getLogger("EditorModel");
    /**
     * Node id generation
     */
    private int lastNodeId = 0;
    /**
     * Importer for old models
     */
    private EAdventure1XImporter importer;
    /**
     * Reader for DOM models
     */
    EAdAdventureDOMModelReader reader;
    /**
     * Writer for DOM models
     */
    EAdAdventureModelWriter writer;
    /**
     * Dependency graph; main model structure
     */
    private ListenableDirectedGraph<DependencyNode, DependencyEdge> g;
    /**
     * Quick reference for node retrieval; uses editor-ids
     */
    private TreeMap<Integer, DependencyNode> nodesById;
    /**
     * Contents do not guarantee "unique IDs"
     */
    private HashMap<Object, DependencyNode> nodesByContent;
    /**
     * high-level representations suitable for editing,
     * persisted in editor.xml
     */
    private HashMap<DependencyNode, ArrayList<DependencyNode>> editorNodes;
    /**
     * The root of the graph
     */
    private DependencyNode root;
    /**
     * Search index
     */
    private ModelIndex nodeIndex;
    /**
     * Used to quickly search editor-nodes for editor-ids
     */
    private Pattern editorIdPattern;
    /**
     * Current project directory; used to save & load
     */
    private File saveDir;
    /**
     * Engine model
     */
    private EAdAdventureModel engineModel;
    /**
     * Listeners for long operations
     */
    private ArrayList<ModelProgressListener> progressListeners = new ArrayList<ModelProgressListener>();


    /**
     * Constructor. Does not do much beyond initializing fields.
     *
     * @param reader
     * @param importer
     * @param writer
     */
    @Inject
    public EditorModel(
            EAdAdventureDOMModelReader reader,
            EAdventure1XImporter importer,
            EAdAdventureModelWriter writer) {
        g = new ListenableDirectedGraph<DependencyNode, DependencyEdge>(DependencyEdge.class);
        this.reader = reader;
        this.importer = importer;
        this.writer = writer;

        this.nodesById = new TreeMap<Integer, DependencyNode>();
        this.nodesByContent = new HashMap<Object, DependencyNode>();
        this.nodeIndex = new ModelIndex();
        this.saveDir = null;
    }

    /**
     * Gets a unique ID. All new DependencyNodes should get their IDs this way. Uses
     * a static field to store the last assigned ID; standard disclaimers on
     * thread-safety and class-loaders apply.
     *
     * @return
     */
    public int generateId() {
        return lastNodeId++;
    }

    /**
     * Makes sure that the returned id contains an eid-prefix.
     *
     * @see createOrUnfreeze for details
     * @param id to alter
     * @param eid to insert (not inserted if already present)
     * @return the (possibly-altered) eid
     */
    private String decorateIdWithEid(String id, int eid) {
        Matcher m = editorIdPattern.matcher(id);
        if (m.find() && m.group(1).equals("" + eid)) {
            return id;
        } else {
            return "__" + eid + "__" + id;
        }
    }

    /**
     * Wraps a targetContent in an DependencyNode. If the content is of a type that
     * has extra editor data associated (a subclass of EAdElement), and this
     * editor data is available, it is used; otherwise, a new DependencyNode is
     * created.
     *
     * @param targetContent to wrap
     * @return a new or old editorNode to wrap that content
     */
    @SuppressWarnings("unchecked")
    private DependencyNode createOrUnfreezeNode(Object targetContent) {

        DependencyNode node;
        int eid;
        if (targetContent instanceof EAdElement) {
            EAdElement e = (EAdElement) targetContent;

            // get the editor-id - either new or reused
            if (editorIdPattern == null) {
                editorIdPattern = Pattern.compile("__([0-9]+)__.*");
            }
            Matcher m = editorIdPattern.matcher(e.getId());
            if (m.find()) {
                // content is eadElement, and has editor-anotation: unfreeze
                eid = Integer.parseInt(m.group(1));
                node = nodesById.get(eid);
                if (node == null) {
//					when loading, nodes not mentioned in the editor.xml file will not be found
//					logger.warn("could not unfreeze id {}", eid);
//					if (logger.isDebugEnabled()) {
//						StringBuilder allEntries = new StringBuilder();
//						for (Entry<Integer, DependencyNode> en : nodesById.entrySet()) {
//							Object o = en.getValue().getContent();
//							allEntries.append(en.getKey()).append(" -> ")
//								.append(o.getClass().getSimpleName())
//								.append(" (").append(o.hashCode()).append(")\n");
//						}
//						logger.debug("Entries: \n{}", allEntries.toString());
//					}
//				    therefore, they have to be created
                    e.setId(decorateIdWithEid(e.getId(), eid));
                    node = new EngineNode(eid, e);
                    nodesById.put(eid, node);
                }
            } else {
                // content is eadElement, but has no editor-annotation: add it
                eid = generateId();
                e.setId(decorateIdWithEid(e.getId(), eid));
                node = new EngineNode(eid, e);
                nodesById.put(eid, node);
            }
        } else {
            // content cannot have editor-annotations at all
            eid = generateId();
            node = new EngineNode(eid, targetContent);
            nodesById.put(eid, node);
        }

        // assign content (may overwrite existing content; no big deal)
        node.setContent(targetContent);
        nodesByContent.put(targetContent, node);
        return node;
    }

    /**
     * Adds a new EditorNode
     */
    public void registerEditorNode(EditorNode e, Collection<DependencyNode> nodes) {
        g.addVertex(e);
        nodesById.put(e.getId(), e);
        for (DependencyNode n : nodes) {
            e.addChild(n);
            g.addEdge(e, n, new DependencyEdge(e.getClass().getName()));
        }
    }

    /**
     * Attempts to add a new node-and-edge to the graph; use only during initial
     * model-building. The edge may be null (for the root).
     *
     * @return the new node if added, or null if already existing (and
     * therefore, it makes no sense to continue adding recursively from there
     * on).
     */
    private DependencyNode addNode(DependencyNode source, String type, Object targetContent) {
        boolean alreadyKnown = (nodesByContent.containsKey(targetContent));
        DependencyNode target = alreadyKnown
                ? nodesByContent.get(targetContent)
                : createOrUnfreezeNode(targetContent);

        if (!alreadyKnown) {
            g.addVertex(target);
        }

        if (source != null) {
            g.addEdge(source, target, new DependencyEdge(type));
        } else {
            root = target;
        }

        if (!alreadyKnown) {
            return target;
        } else {
            return null;
        }
    }

    /**
     * Visits a node
     *
     * @see ModelVisitor#visitObject
     */
    @Override
    public boolean visitObject(Object target, Object source, String sourceName) {
        logger.debug("Visiting object: '{}'--['{}']-->'{}'",
                new Object[]{source, sourceName, target});

        // source is only null for root node
        if (source == null) {
            // should keep on drilling, but otherwise nothing to do here
            addNode(null, null, target);
            return true;
        }

        DependencyNode sourceNode = (source != null)
                ? nodesByContent.get(source) : null;
        DependencyNode e = addNode(sourceNode, sourceName, target);

        if (e != null) {
            nodeIndex.addProperty(e, ModelIndex.editorIdFieldName,
                    "" + e.getId(), false);
            nodesByContent.put(target, e);
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
    public void visitProperty(Object target, String propertyName, String textValue) {
        logger.debug("Visiting property: '{}' :: '{}' = '{}'",
                new Object[]{target, propertyName, textValue});
        DependencyNode e = nodesByContent.get(target);
        nodeIndex.addProperty(e, propertyName, textValue, true);
    }

    /**
     * Exports the editor model into a zip file.
     * @param target; if null, previous target is assumed
     * @throws IOException
     */
    public void exportGame(File target) throws IOException {
        importer.createGameFile(
                (EAdAdventureModel) root.getContent(),
                saveDir.getAbsolutePath(), target.getAbsolutePath(),
                ".eap", "Editor project, exported", true);
    }

    /**
     * Saves the editor model. Save will contain a normal EAdModel, plus resources,
     * plus editor-specific model nodes. Does not include anything presentation-
     * related; that should be appended via
     * FileUtils.appendEntryToZip(target, ...)
     *
     * @param target; if null, previous target is assumed
     * @throws IOException
     */
    public void save(File target) throws IOException {

        updateProgress(5, "Commencing save ...");
        if (target != null && saveDir != target) {
            // copy over all resource-files first
            updateProgress(10, "Copying resources to new destination ...");
            FileUtils.copyRecursive(saveDir, null, target);
        } if (target == null && saveDir != null) {
            target = saveDir;
        } else {
            throw new IllegalArgumentException("Cannot save() without knowing where!");
        }

        // write main xml
        updateProgress(50, "Writing engine model ...");
        boolean ok = writer.write((EAdAdventureModel) root.getContent(),
                new File(target, ProjectFiles.DATA_FILE).toURI());

        // write extra xml file to it
        updateProgress(80, "Writing editor model ...");
        int mappings = 0;
        if (ok) {
            try {
                mappings = writeMappingsToFile(new File(target, "editor.xml"));
            } catch (IOException ioe) {
                logger.error("Could not write editor.xml file to {}", target, ioe);
            }
        }
        saveDir = target;
        updateProgress(100, "... save complete.");

        logger.info("Wrote editor data from {} to {}: {} total objects, {} editor mappings",
                new Object[]{saveDir, target, nodesById.size(), mappings});
    }

    /**
     * Writes the editor mappings to an editor.xml file.
     * @param dest
     * @return number of mappings written
     */
    private int writeMappingsToFile(File dest) throws IOException {
        int mappings = 0;
        StringBuilder sb = new StringBuilder();
        for (DependencyNode n : nodesById.values()) {
            if (n instanceof EditorNode) {
                logger.debug("Writing editorNode of type {} with id {}",
                        new Object[]{n.getClass(), n.getId()});
                ((EditorNode) n).write(sb);
                mappings++;
            }
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(
                sb.toString().getBytes("UTF-8"));
        OutputStream fos = new FileOutputStream(dest);
        FileUtils.copy(bis, fos);
        return mappings;
    }

    /**
     * Flushes the model.
     */
    private void clear() {
        nodesByContent.clear();
        nodesById.clear();
        nodeIndex = new ModelIndex();
        g.removeAllEdges(new HashSet<DependencyEdge>(g.edgeSet()));
        g.removeAllVertices(new HashSet<DependencyNode>(g.vertexSet()));
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

        // clear caches
        clear();

        saveDir = sourceDir;
        updateProgress(10, "Reading engine model ...");
        engineModel = reader.read(
                new File(saveDir, ProjectFiles.DATA_FILE).toURI());

        updateProgress(50, "Reading editor model ...");
        logger.info("Model loaded; building graph...");
        ModelVisitorDriver driver = new ModelVisitorDriver();
        driver.visit(engineModel, this);
        this.root = nodesByContent.get(engineModel);

        updateProgress(90, "Indexing model ...");
        nodeIndex.firstIndexUpdate(g.vertexSet());

        logger.info("Editor model loaded: {} nodes, {} edges",
                new Object[]{g.vertexSet().size(), g.edgeSet().size()});

        updateProgress(100, "... load complete.");
    }

    /**
     * Returns a file that is relative to this save-file
     * @param name of file to return, relative to save-file
     */
    public File relativeFile(String name) {
        if (saveDir.exists() && saveDir.isDirectory()) {
            return new File(saveDir, name);
        } else {
            throw new IllegalArgumentException("Nothing loaded, loadRelative not available");
        }
    }

    /**
     * Loads data from an EAdventure1.x game file. Saves this as an
     * EAdventure 2.x editor file.
     *
     * @param fin old-version file to import from
     * @param fout target folder to build into
     */
    public void loadFromImportFile(File fin, File fout) throws IOException {
        logger.info("Loading editor model from EAD 1.x import '{}' into '{}'...",
                fin, fout);

        // clear caches
        clear();

        ProgressProxy pp = new ProgressProxy();
        importer.addProgressListener(pp);
        engineModel = importer.importGame(fin.getAbsolutePath(),
                fout.getAbsolutePath());
        importer.removeProgressListener(pp);

        updateProgress(50, "Reading editor model ...");
        logger.info("Model loaded; building graph...");
        ModelVisitorDriver driver = new ModelVisitorDriver();
        driver.visit(engineModel, this);
        this.root = nodesByContent.get(engineModel);

        updateProgress(90, "Indexing model ...");
        nodeIndex.firstIndexUpdate(g.vertexSet());

        logger.info("Editor model loaded: {} nodes, {} edges",
                new Object[]{g.vertexSet().size(), g.edgeSet().size()});
        saveDir = fout;

        updateProgress(100, "... load complete.");
    }



    // ---- basic access
    public DependencyNode getNode(int id) {
        return nodesById.get(id);
    }

    public int getIdFor(Object o) {
        DependencyNode n = nodesByContent.get(o);
        if (n != null) {
            return n.getId();
        } else {
            return -1;
        }
    }

    public EAdAdventureModel getEngineModel() {
        return engineModel;
    }

    // ---- search-related functions API ----
    /**
     * Queries all fields in all nodes for the provided text.
     *
     * @param queryText
     * @return a list of all matching nodes, ranked by relevance
     */
    public List<DependencyNode> searchAll(String queryText) {
        return nodeIndex.searchAll(queryText, nodesById);
    }

    /**
     * Queries a given field in all nodes for the provided text.
     *
     * @param queryText
     * @return a list of all matching nodes, ranked by relevance
     */
    public List<DependencyNode> search(String field, String queryText) {
        return nodeIndex.search(field, queryText, nodesById);
    }

    /**
     * Retrieves a list of all indexed fields.
     */
    public List<String> getAllSearchableFields() {
        return nodeIndex.getIndexedFieldNames();
    }

    @Override
    public DependencyNode getElement(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        char c = id.charAt(0);
        if (Character.isLetter(c)) {
            switch (c) {
                case 'q':
                    return new QueryNode(this, id.substring(1));
                case 't': // type query
                case 'f': // field query
                    throw new IllegalArgumentException("Not yet implemented");
                default:
                    throw new IllegalArgumentException("Expected number or q*,t*,f* queries");
            }
        } else if (Character.isDigit(c)) {
            int eid = Integer.parseInt(id);
            return getNode(eid);
        } else {
            throw new IllegalArgumentException("Expected number or q*,t*,f* queries");
        }
    }

    @Override
    public DependencyNode createElement(Class<? extends DependencyNode> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DependencyNode copyElement(DependencyNode e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	public void addProgressListener(ModelProgressListener progressListener) {
		progressListeners.add(progressListener);
	}

	public void removeProgressListener(ModelProgressListener progressListener) {
		progressListeners.remove(progressListener);
	}

    public void updateProgress(int progress, String text) {
        logger.debug("Model progress update: {}", text);
		for (ModelProgressListener l : progressListeners) {
			l.update(progress, text);
		}
	}

    /**
     * Re-issues importer progress updates as own updates
     */
    public class ProgressProxy implements EAdventure1XImporter.ImporterProgressListener {

        @Override
        public void update(int progress, String text) {
            updateProgress(progress, text);
        }
    }

	public static interface ModelProgressListener {
		public void update(int progress, String text);
	}
}
