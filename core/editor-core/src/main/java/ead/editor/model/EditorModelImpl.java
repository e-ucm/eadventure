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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.graph.ListenableDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import ead.common.interfaces.features.Identified;
import ead.common.model.elements.EAdAdventureModel;
import ead.editor.EditorStringHandler;
import ead.editor.model.nodes.DependencyEdge;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EditorNode;
import ead.editor.model.nodes.QueryNode;
import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

/**
 * Contains a full model of what is being edited. This is a super-set of an
 * EAdAdventureModel, encompassing both engine-related model objects and
 * resources, assets, and strings. Everything is searchable, and dependencies
 * are tracked as objects are changed.
 *
 * @author mfreire
 */
public class EditorModelImpl implements EditorModel {

	private static final Logger logger = LoggerFactory.getLogger("EditorModel");

	/**
	 * A large number, hard to reach counting from 0 upwards
	 */
	public static final int intermediateIDPoint = 1 << 24;
	/**
	 * Id used for 'bad' elements; warnings will be shown if forced to used it
	 */
	public static final int badElementId = -1;

	/**
	 * Node id generation: default ids
	 */
	private int lastElementNodeId = 0;
	/**
	 * Node id generation: transients. Not serialized on save/load.
	 * Should never touch default ids.
	 */
	private int lastTransientNodeId = intermediateIDPoint;

	/**
	 * Dependency graph; main model structure
	 */
	private ListenableDirectedGraph<DependencyNode, DependencyEdge> g;
	/**
	 * Quick reference for node retrieval; uses editor-ids.
	 */
	private TreeMap<Integer, DependencyNode> nodesById;
	/**
	 * Quick reference for node retrieval; uses contents. Available only
	 * for content-types that do not have embedded editor-ids
	 */
	private HashMap<Object, DependencyNode> nodesByContent;

	/**
	 * The root of the graph; contains the engineModel
	 */
	private DependencyNode root;
	/**
	 * Internationalized strings
	 */
	private EditorStringHandler stringHandler;
	/**
	 * Game properties. Warning: does not preserve comments
	 */
	private HashMap<String, String> engineProperties;

	/**
	 * Search index
	 */
	private ModelIndex nodeIndex;
	/**
	 * Used to quickly search editor-nodes for editor-ids
	 */
	public static final Pattern editorIdPattern = Pattern
			.compile("__([0-9]+)__.*");
	/**
	 * Engine model
	 */
	private EAdAdventureModel engineModel;

	/**
	 * Loader - in charge  of save/load operations
	 */
	private EditorModelLoader loader;

	/**
	 * Listeners for long operations
	 */
	private ArrayList<ModelProgressListener> progressListeners = new ArrayList<ModelProgressListener>();

	/**
	 * Listeners for model changes
	 */
	private ArrayList<ModelListener> modelListeners = new ArrayList<ModelListener>();

	/**
	 * Constructor. Does not do much beyond initializing fields.
	 *
	 * @param reader
	 * @param importer
	 * @param writer
	 */
	@Inject
	public EditorModelImpl(EditorModelLoader loader) {
		g = new ListenableDirectedGraph<DependencyNode, DependencyEdge>(
				DependencyEdge.class);
		this.nodesById = new TreeMap<Integer, DependencyNode>();
		this.nodesByContent = new HashMap<Object, DependencyNode>();
		this.nodeIndex = new ModelIndex();
		this.loader = loader;
	}

	// ----- nodes

	@Override
	public DependencyNode getNode(int id) {
		return nodesById.get(id);
	}

	/**
	 * Gets a unique ID. All new DependencyNodes should get their IDs this way.
	 * Uses a static field to store the last assigned ID; standard disclaimers
	 * on thread-safety and class-loaders apply.
	 *
	 * @param targetObject
	 *            an engine object, or null for synthetic elements
	 * @return
	 */
	@Override
	public int generateId(Object targetObject) {

		int assigned = (targetObject == null || targetObject instanceof Identified) ? lastElementNodeId++
				: lastTransientNodeId++;

		if (nodesById.containsKey(assigned)) {
			logger.error("Duplicate ID {} for object {} (was {})",
					new Object[] { assigned, targetObject,
							nodesById.get(assigned) });
			// will keep on trying until it finds a free id
			return generateId(targetObject);
		}
		return assigned;
	}

	/**
	 * Makes sure that the returned id contains an eid-prefix.
	 *
	 * @see createOrUnfreeze for details
	 * @param id
	 *            to alter
	 * @param eid
	 *            to insert (not inserted if already present)
	 * @return the (possibly-altered) eid
	 */
	public static String decorateIdWithEid(String id, int eid) {
		Matcher m = editorIdPattern.matcher(id);
		if (m.find() && m.group(1).equals("" + eid)) {
			return id;
		} else {
			return "__" + eid + "__" + id;
		}
	}

	/**
	 * Returns the editor-id of the object
	 * @param o
	 * @return  editorId if the object has a valid editorId (Identified or
	 * the StringHandler) - or badElementId otherwise.
	 */
	@Override
	public int getEditorId(Object o) {
		if (o instanceof Identified) {
			Identified i = (Identified) o;

			Matcher m = editorIdPattern.matcher(i.getId());
			if (m.find()) {
				return Integer.parseInt(m.group(1));
			}
		}

		return badElementId;
	}

	/**
	 * Returns the DependencyNode for an object that is wrapped in an editorNode.
	 * This works in two ways. First, if it has an editor-id tag, it is used.
	 * Otherwise, it must have been an unmarked object (list, map, resource, ...);
	 * and it the unpersisted-to-editorNode map is used instead.
	 */
	@Override
	public DependencyNode getNodeFor(Object content) {
		int eid = getEditorId(content);
		if (eid < 0) {
			return nodesByContent.get(content);
		} else {
			return nodesById.get(eid);
		}
	}

	@Override
	public List<DependencyNode> incomingDependencies(DependencyNode node) {
		ArrayList<DependencyNode> ns = new ArrayList<DependencyNode>();
		for (DependencyEdge e : g.incomingEdgesOf(node)) {
			ns.add(g.getEdgeSource(e));
		}
		return ns;
	}

	@Override
	public List<DependencyNode> outgoingDependencies(DependencyNode node) {
		ArrayList<DependencyNode> ns = new ArrayList<DependencyNode>();
		for (DependencyEdge e : g.outgoingEdgesOf(node)) {
			ns.add(g.getEdgeTarget(e));
		}
		return ns;
	}

	// ----- Internal access (mostly for loading & saving)

	/**
	 * Flushes the model.
	 */
	public void clear() {
		lastElementNodeId = 0;
		lastTransientNodeId = intermediateIDPoint;
		nodesById.clear();
		nodesByContent.clear();
		nodeIndex = new ModelIndex();
		g.removeAllEdges(new HashSet<DependencyEdge>(g.edgeSet()));
		g.removeAllVertices(new HashSet<DependencyNode>(g.vertexSet()));
		g = new ListenableDirectedGraph<DependencyNode, DependencyEdge>(
				DependencyEdge.class);
	}

	public ListenableDirectedGraph<DependencyNode, DependencyEdge> getGraph() {
		return g;
	}

	public DependencyNode getRoot() {
		return root;
	}

	public void setRoot(DependencyNode root) {
		this.root = root;
	}

	public void setLastElementNodeId(int lastElementNodeId) {
		this.lastElementNodeId = lastElementNodeId;
	}

	public void setEngineModel(EAdAdventureModel engineModel) {
		this.engineModel = engineModel;
	}

	public TreeMap<Integer, DependencyNode> getNodesById() {
		return nodesById;
	}

	public HashMap<Object, DependencyNode> getNodesByContent() {
		return nodesByContent;
	}

	public ModelIndex getNodeIndex() {
		return nodeIndex;
	}

	public File getResourcePath() {
		return loader.getSaveDir();
	}

	public void setStringHandler(EditorStringHandler stringHandler) {
		this.stringHandler = stringHandler;
	}

	public void setEngineProperties(HashMap<String, String> engineProperties) {
		this.engineProperties = engineProperties;
	}

	// ----- ModelAccessor

	/**
	 * Gets the model element with id 'id'. Also generates synthetic nodes
	 * on-demand
	 * @throws NoSuchElementException if not found.
	 * @param id of element (assigned by editor when project is imported)
	 * @return element with id as its editor-id
	 */
	@Override
	public DependencyNode getElement(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}

		int eid = Integer.parseInt(id);
		return getNode(eid);
	}

	@Override
	public DependencyNode createElement(Class<? extends DependencyNode> type) {
		DependencyNode node = null;
		try {
			Constructor c = type.getConstructor(Integer.TYPE);
			node = (DependencyNode) c.newInstance(generateId(null));
		} catch (Exception e) {
			logger.error("Cannot create EditorNode of class {}",
					type.getName(), e);
		}
		return node;
	}

	@Override
	public DependencyNode copyElement(DependencyNode e) {
		throw new UnsupportedOperationException("Not yet supported");
	}

	// ----- EditorNode manipulation

	/**
	 * Adds a new EditorNode to the dependency-tracking graph
	 */
	public void registerEditorNodeWithGraph(EditorNode e) {
		nodesById.put(e.getId(), e);
		logger.debug("registering {}", e.getTextualDescription(this));
		g.addVertex(e);
		for (DependencyNode n : e.getContents()) {
			logger.debug("\ttarget is {}", n.getTextualDescription(this));
			g.addEdge(e, n, new DependencyEdge(e.getClass().getName()));
		}
	}

	/**
	 * Replaces a node for another node. Incoming and outgoing references are
	 * retained;
	 * @param n
	 * @param replacement
	 */
	private void replaceVertex(DependencyNode n, DependencyNode replacement) {
		ArrayList<DependencyEdge> es = new ArrayList<DependencyEdge>();
		es.addAll(g.edgesOf(n));
		for (DependencyEdge e : es) {
			logger.debug("Fixing up edge {} ---[{}]---> {}");
			DependencyNode source = g.getEdgeSource(e);
			DependencyNode target = g.getEdgeTarget(e);
			if (source == n) {
				source = replacement;
			} else {
				target = replacement;
			}
			g.addEdge(source, target, new DependencyEdge(e.getType()));
		}
		g.removeAllEdges(es);
	}

	// -------- saving, loading, and engine access

	@Override
	public EditorModelLoader getLoader() {
		loader.setModel(this);
		nodeIndex.setModel(this);		
		return loader;
	}

	@Override
	public EAdAdventureModel getEngineModel() {
		return engineModel;
	}

	@Override
	public EditorStringHandler getStringHandler() {
		return stringHandler;
	}

	@Override
	public HashMap<String, String> getEngineProperties() {
		return engineProperties;
	}

	// ---- search-related functions API ----

	/**
	 * Queries all fields in all nodes for the provided text. This
	 * variant provides details of any matches.
	 *
	 * @param queryText
	 * @return a list of all matching nodes, ranked by relevance
	 */
	@Override
	public ModelIndex.SearchResult search(ModelQuery query) {
		return nodeIndex.search(query);
	}

	/**
	 * Retrieves a list of all indexed fields.
	 */
	public List<String> getAllSearchableFields() {
		return nodeIndex.getIndexedFieldNames();
	}

	// ----- progress -----

	@Override
	public void addProgressListener(ModelProgressListener progressListener) {
		progressListeners.add(progressListener);
	}

	@Override
	public void removeProgressListener(ModelProgressListener progressListener) {
		progressListeners.remove(progressListener);
	}

	public void updateProgress(int progress, String text) {
		logger.debug("Model progress update: {}", text);
		for (ModelProgressListener l : progressListeners) {
			l.update(progress, text);
		}
	}

	// ----- progress -----

	@Override
	public void addModelListener(ModelListener modelListener) {
		modelListeners.add(modelListener);
	}

	@Override
	public void removeModelListener(ModelListener modelListener) {
		modelListeners.remove(modelListener);
	}

	@Override
	public void fireModelEvent(ModelEvent event) {
		logger.info("{} listeners for model-event: {}", new Object[] {
				modelListeners.size(), event });
		for (ModelListener l : modelListeners) {
			logger.info("--> now delivering to {}", l);
			l.modelChanged(event);
		}
	}
}
