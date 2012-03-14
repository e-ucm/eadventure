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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import ead.common.importer.EAdventure1XImporter;
import ead.common.importer.ImporterConfigurationModule;
import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.common.writer.EAdAdventureModelWriter;
import ead.editor.Log4jConfig;
import ead.editor.Log4jConfig.Slf4jLevel;
import ead.editor.model.visitor.ModelVisitor;
import ead.editor.model.visitor.ModelVisitorDriver;
import ead.engine.core.platform.module.DesktopAssetHandlerModule;
import ead.engine.core.platform.module.DesktopModule;
import ead.engine.core.platform.modules.BasicGameModule;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains a full model of what is being edited. This is a super-set of an
 * EAdAdventureModel, encompassing both traditional model objects and resources,
 * assets, and strings. Everything is searchable, and dependencies are tracked
 * as objects are changed.
 *
 * @author mfreire
 */
public class EditorModel implements ModelVisitor {

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
	 * Folder where resources are stored
	 * (under 
	 */
	
	/**
	 * Dependency graph; main model structure
	 */
	private ListenableDirectedGraph<EditorNode, EditorEdge> g;
    /**
     * Quick reference for node retrieval
     */
    private TreeMap<Integer, EditorNode> nodesById;
    /**
     * Contents do not guarantee "unique IDs"
     */
    private HashMap<Object, EditorNode> nodesByContent;
    /**
     * The root of the graph
     */
    private EditorNode root;
    /**
     * Search index
     */
    private ModelIndex nodeIndex;
    /**
     * Used to quickly search editor-nodes for editor-ids
     */
    private Pattern editorIdPattern;

	/**
	 * Constructor. Does not do much beyond initializing fields.
	 * @param reader 
	 * @param importer 
	 * @param writer 
	 */
    @Inject
    public EditorModel(
            EAdAdventureDOMModelReader reader,
            EAdventure1XImporter importer,
            EAdAdventureModelWriter writer) {
        g = new ListenableDirectedGraph<EditorNode, EditorEdge>(EditorEdge.class);
        this.reader = reader;
        this.importer = importer;
        this.writer = writer;

        this.nodesById = new TreeMap<Integer, EditorNode>();
        this.nodesByContent = new HashMap<Object, EditorNode>();
        this.nodeIndex = new ModelIndex();
    }

	/**
	 * Loads data from an EAdventure1.x game file.
	 * 
	 * @param f 
	 */
    public void loadFromImportFile(File f) {
        logger.info("Loading editor model from '{}'...", f);
        EAdAdventureModel m = importer.importGame(f.getAbsolutePath(), "/tmp/imported");
        
		logger.info("Model loaded; building graph...");		
        ModelVisitorDriver driver = new ModelVisitorDriver();
        driver.visit(m, this);
		this.root = nodesByContent.get(m);
		nodeIndex.firstIndexUpdate(g.vertexSet());
		
        logger.info("Editor model loaded: {} nodes, {} edges",
                new Object[]{g.vertexSet().size(), g.edgeSet().size()});
    }

	/**
	 * Gets a unique ID. All new EditorNodes should get their IDs this way.
	 * Uses a static field to store the last assigned ID; standard disclaimers
	 * on thread-safety and classloaders apply.
	 * 
	 * @return 
	 */
    private int generateId() {
        return lastNodeId++;
    }

    /**
     * Makes sure that the returned id contains an eid-prefix. @see
     * createOrUnfreeze for details
     * @param id to alter
     * @param eid to insert (not inserted if already present)
     * @return the (possibly-altered) eid
     */
    private String decorateIdWithEid(String id, int eid) {
        Matcher m = editorIdPattern.matcher(id);
        if (m.find() && m.group(1).equals(""+eid)) {
            return id;
        } else {
            return "__" + eid + "__" + id;
        }
    }

    /**
     * Wraps a targetContent in an EditorNode. If the content is of a
     * type that has extra editor data associated (a subclass of EAdElement),
     * and this editor data is available, it is used; otherwise, a new
     * EditorNode is created.
     * @param targetContent to wrap
     * @return a new or old editorNode to wrap that content
     */
    private EditorNode createOrUnfreezeNode(Object targetContent) {

        EditorNode node;
        if (targetContent instanceof EAdElement) {
            EAdElement e = (EAdElement) targetContent;

            // get the editor-id - either new or reused
            if (editorIdPattern == null) {
                editorIdPattern = Pattern.compile("__([0-9]+)__.*");
            }
            Matcher m = editorIdPattern.matcher(e.getId());
            if (m.find()) {
                int eid = Integer.parseInt(m.group(1));
                node = nodesById.get(eid);
                node.setContent(targetContent);
            } else {
                int eid = generateId();
                // associate new id with element
                e.setId(decorateIdWithEid(e.getId(), generateId()));
                node = new EditorNode(eid, e);
            }
        } else {
            int eid = generateId();
            node = new EditorNode(eid, targetContent);
        }

        return node;
    }

    /**
     * Attempts to add a new node-and-edge to the graph; use only during initial
     * model-building. The edge may be null (for the root).
     *
     * @return the new node if added, or null if already existing (and
     * therefore, it makes no sense to continue adding recursively from there
     * on).
     */
    private EditorNode addNode(EditorNode source, String type, Object targetContent) {
        boolean alreadyKnown = (nodesByContent.containsKey(targetContent));
        EditorNode target = alreadyKnown
                ? nodesByContent.get(targetContent)
                : createOrUnfreezeNode(targetContent);

        if (!alreadyKnown) {
            g.addVertex(target);
        }

        if (source != null) {
            g.addEdge(source, target, new EditorEdge(type));
        } else {
            root = target;
        }

        if (!alreadyKnown) {
            nodesById.put(target.getId(), target);
            nodesByContent.put(target.getContent(), target);
            return target;
        } else {
            return null;
        }
    }

    /**
     * Visits a node
	 * @see ModelVisitor#visitObject
     */
    @Override
    public boolean visitObject(Object target, Object source, String sourceName) {
        logger.debug("Visiting object: '{}'--['{}']-->'{}'",
                new Object[]{source, sourceName, target});

        // source is null for root node
        EditorNode sourceNode = (source != null)
                ? nodesByContent.get(source) : null;
        EditorNode e = addNode(sourceNode, sourceName, target);

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
	 * @see ModelVisitor#visitProperty
     */
    @Override
    public void visitProperty(Object target, String propertyName, String textValue) {
        logger.debug("Visiting property: '{}' :: '{}' = '{}'",
                new Object[]{target, propertyName, textValue});
        EditorNode e = nodesByContent.get(target);
        nodeIndex.addProperty(e, propertyName, textValue, true);
    }

    /**
     * Saves the editor model. Contains a normal EAdModel plus editing
     *
     * @param target
     * @throws IOException
     */
    public void save(File target) throws IOException {
        /*
         * similar to import-write, but also adds one or more editor.xml files
         */
		
		// save the model data
		boolean ok = importer.createGameFile((EAdAdventureModel)root.getContent(), 
				target.getParent(), target.getName(), ".eap", "Editor project");
		
		// save the editor data
		// open the zip file
		// write xml files to it
		// close the zip file
		
		// 
    }

    /**
     * Loads the editor model. Discards the current editing session The file
     * must have been built with save
     *
     * @param source
     * @throws IOException
     */
    public void load(File source) throws IOException {
        nodesByContent.clear();
        nodesById.clear();
        nodeIndex = new ModelIndex();
        /*
         * similar to import-read, but loads from v2 .ead file, and uses the
         * editor.xml file to restore editor-ids (necessary for docking-frames
         * support)
         */
        throw new UnsupportedOperationException("not yet implemented");
    }
		
    private void testSearch() {
        //for (EditorNode e : search("id", "elem*")) {
        for (EditorNode e : nodeIndex.searchAll("disp_x", nodesById)) {
            logger.info("found: " + e.getId() + " "
                    + e.getContent().getClass().getSimpleName() + " "
                    + e.getContent() + " :: "
                    + (e.getContent() instanceof EAdElement ? ((EAdElement) e.getContent()).getId() : "??"));
        }
    }

    public static void main(String[] args) {

        Log4jConfig.configForConsole(Slf4jLevel.Fatal, new Object[]{
                    "ModelVisitorDriver", Slf4jLevel.Info,
                    "EditorModel", Slf4jLevel.Debug
                });

        Injector injector = Guice.createInjector(
                new ImporterConfigurationModule(),
                new BasicGameModule(),
                new DesktopModule(),
                new DesktopAssetHandlerModule());
        EditorModel model = injector.getInstance(EditorModel.class);

        File f = new File("/home/mfreire/code/e-ucm/e-adventure-1.x/games/PrimerosAuxiliosGame.ead");
        model.loadFromImportFile(f);

        model.testSearch();
        //model.exportGraph(new File("/tmp/exported.graphml"));
    }
}
