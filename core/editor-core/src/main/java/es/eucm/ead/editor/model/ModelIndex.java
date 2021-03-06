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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.visitor.ModelVisitor;
import es.eucm.ead.editor.model.visitor.ModelVisitorDriver;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows easy search operations on the model. Uses Lucene for indexing and
 * retrieval.
 *
 * @author mfreire
 */
public class ModelIndex implements EditorModel.ModelListener {

	private static Logger logger = LoggerFactory.getLogger(ModelIndex.class);
	public static final String editorIdFieldName = "editor-id";

	public static final String editorIdQueryField = "eid";
	public static final String hasContentClassQueryField = "has";
	public static final String isClassQueryField = "is";

	/**
	 * Lucene index
	 */
	private Directory searchIndex;
	/**
	 * Lucene updater
	 */
	private IndexWriter indexWriter;
	/**
	 * Max search hits in an ordered query
	 */
	private static final int MAX_SEARCH_HITS = 100;
	/**
	 * Query parser for 'all fields' queries
	 */
	private QueryParser queryParser;
	/**
	 * Field analyzer
	 */
	private Analyzer searchAnalyzer;

	/**
	 * Upstream model; listened to, queried occasionally to resolve IDs
	 */
	private EditorModelImpl model;

	/**
	 * Configure Lucene indexing
	 */
	public ModelIndex() {
		clear();
	}

	/**
	 * Purges the contents of this modelIndex
	 */
	public void clear() {
		try {
			searchIndex = new RAMDirectory();
			// use a very simple analyzer; no fancy stopwords, stemming, ...
			searchAnalyzer = new WhitespaceAnalyzer(Version.LUCENE_35);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35,
					searchAnalyzer);
			indexWriter = new IndexWriter(searchIndex, config);
		} catch (Exception e) {
			logger.error("Could not initialize search index (?)", e);
			throw new IllegalArgumentException(
					"Could not initialize search index (?)", e);
		}
	}

	/**
	 * Adds a property to a node.
	 * @param e the node
	 * @param field name
	 * @param value of property
	 * @param searchable if this field is to be indexed and used in "anywhere"
	 * searches
	 */
	public static void addProperty(DependencyNode e, String field,
			String value, boolean searchable) {

		e.getDoc().add(
				new Field(field, value, Store.YES, searchable ? Index.ANALYZED
						: Index.NO));
	}

	/**
	 * Changes the model indexed by this indexer. Also resets the
	 * @param model
	 */
	void setModel(EditorModelImpl model) {
		if (model != null && this.model == model) {
			// do nothing
		} else if (this.model != null) {
			model.removeModelListener(this);
		} else {
			this.model = model;
			model.addModelListener(this);
		}
	}

	@Override
	public void modelChanged(ModelEvent event) {
		updateNodes(event.getChanged());
	}

	/**
	 * Updates the index regarding a set of nodes. The node documents are
	 * re-indexed by visiting them anew.
	 * @param nodes
	 */
	public void updateNodes(DependencyNode... nodes) {
		DependencyNode last = null;
		try {
			for (DependencyNode e : nodes) {
				logger.info("updating {}", e.getId());
				last = e;
				Term q = new Term(editorIdFieldName, "" + e.getId());
				indexWriter.deleteDocuments(q);
				e.clearDoc();
				ModelVisitorDriver mvd = new ModelVisitorDriver();
				mvd.visit(e, new UpdatePropertiesVisitor(e), model
						.getStringHandler());
				indexWriter.addDocument(e.getDoc());
			}
		} catch (Exception ex) {
			logger.error("Error adding search information for node {}", last
					.getId(), ex);
		}
		try {
			indexWriter.commit();
		} catch (Exception ex) {
			logger.error("Error commiting search information", ex);
		}
	}

	/**
	 * Index an DependencyNode for later search
	 */
	public void firstIndexUpdate(Collection<DependencyNode> nodes) {
		for (DependencyNode e : nodes) {
			Document doc = e.getDoc();
			logger.trace("Writing index for {} of class {}", new Object[] {
					e.getId(), e.getContent().getClass().getSimpleName() });
			try {
				indexWriter.addDocument(doc);
			} catch (Exception ex) {
				logger.error("Error adding search information for node {}", e
						.getId(), ex);
			}
		}
		try {
			indexWriter.commit();
		} catch (Exception ex) {
			logger.error("Error commiting search information", ex);
		}
	}

	/**
	 * Lazily create or return the query parser
	 */
	private QueryParser getQueryAllParser() {

		if (queryParser == null) {
			try {
				IndexReader reader = IndexReader.open(searchIndex);

				ArrayList<String> al = new ArrayList<String>(reader
						.getFieldNames(IndexReader.FieldOption.INDEXED));
				String[] allFields = al.toArray(new String[al.size()]);
				if (logger.isDebugEnabled()) {
					Arrays.sort(allFields);
					logger.debug("enumerating indexed fields");
					for (String name : allFields) {
						logger.debug("  indexed field: '{}'", name);
					}
				}
				queryParser = new MultiFieldQueryParser(Version.LUCENE_35,
						allFields, searchAnalyzer);
			} catch (IOException ioe) {
				logger.error("Error constructing query parser", ioe);
			}
		}
		return queryParser;
	}

	/**
	 * Get names of all indexed fields.
	 * @return names of all indexed fields.
	 */
	public List<String> getIndexedFieldNames() {
		try {
			IndexReader reader = IndexReader.open(searchIndex);
			return new ArrayList<String>(reader
					.getFieldNames(IndexReader.FieldOption.INDEXED));
		} catch (IOException ioe) {
			throw new IllegalArgumentException(
					"Error finding names of indexable fields", ioe);
		}
	}

	/**
	 * An individual node match for a query, with score and matched fields
	 */
	public static class Match implements Comparable<Match> {
		private HashSet<String> fields = new HashSet<String>();
		private double score;
		private DependencyNode node;

		private Match(DependencyNode node, double score, String field) {
			this.node = node;
			this.score = score;
			if (field != null) {
				fields.add(field);
			}
		}

		private void merge(Match m) {
			this.score += m.score;
			this.fields.addAll(m.fields);
		}

		public HashSet<String> getFields() {
			return fields;
		}

		public double getScore() {
			return score;
		}

		public DependencyNode getNode() {
			return node;
		}

		@Override
		public int compareTo(Match o) {
			return Double.compare(o.score, score);
		}
	}

	/**
	 * Represents query results
	 */
	public static class SearchResult {

		private TreeMap<Integer, Match> matches = new TreeMap<Integer, Match>();

		private static final Pattern fieldMatchPattern = Pattern
				.compile("fieldWeight[(]([^:]+):");

		public SearchResult() {
			// used for "empty" searches: no results
		}

		public SearchResult(IndexSearcher searcher, Query query, boolean quick,
				ScoreDoc[] hits, Map<Integer, DependencyNode> nodesById)
				throws IOException {

			try {
				for (ScoreDoc hit : hits) {
					String nodeId;
					nodeId = searcher.doc(hit.doc).get(editorIdFieldName);
					logger.debug("Adding {}", nodeId);
					DependencyNode node = nodesById.get(Integer
							.parseInt(nodeId));
					Match m = new Match(node, hit.score, null);
					matches.put(node.getId(), m);
					if (!quick) {
						fillFieldsForExplanation(searcher.explain(query,
								hit.doc), m);
					} else {
						logger.debug("Not explaining results: quick requested");
					}
				}
				searcher.close();
			} catch (CorruptIndexException e) {
				throw new IOException("Corrupt index", e);
			}
		}

		public final void fillFieldsForExplanation(Explanation e, Match match) {
			String s = e.getDescription();
			logger.debug("Reading explanation for {}: '{}'", new Object[] {
					match.getNode().getId(), s });
			Matcher m = fieldMatchPattern.matcher(s);
			if (m.find()) {
				logger.debug("Adding another match: {}", m.group(1));
				match.getFields().add(m.group(1));
			}

			// recurse
			if (e.getDetails() == null) {
				return;
			}
			for (Explanation se : e.getDetails()) {
				fillFieldsForExplanation(se, match);
			}
		}

		public ArrayList<Match> getMatches() {
			ArrayList<Match> all = new ArrayList<Match>(matches.values());
			Collections.sort(all);
			return all;
		}

		public Match getMatchFor(int id) {
			return matches.get(id);
		}

		public void merge(SearchResult other) {
			for (Map.Entry<Integer, Match> e : other.matches.entrySet()) {
				if (!matches.containsKey(e.getKey())) {
					matches.put(e.getKey(), new Match(e.getValue().getNode(),
							0, null));
				}
				matches.get(e.getKey()).merge(e.getValue());
			}
		}
	}

	/**
	 * Get a (sorted) list of nodes that match a query
	 */
	public SearchResult search(ModelQuery query) {
		logger.info("Querying for {}", query);
		SearchResult r = new SearchResult();
		for (ModelQuery.QueryPart p : query.getQueryParts()) {
			r.merge(search(p.getField(), p.getValue(), false));
		}
		return r;
	}

	public SearchResult searchByClass(String queryText) {
		SearchResult sr = new SearchResult();
		for (DependencyNode n : model.getNodesById().values()) {
			if (n.getClass().getName().indexOf(queryText) != -1) {
				sr.matches.put(n.getId(), new Match(n, 1, isClassQueryField));
			}
		}
		return sr;
	}

	public SearchResult searchByContentClass(String queryText) {
		SearchResult sr = new SearchResult();
		for (DependencyNode n : model.getNodesById().values()) {
			if (n.getContent().getClass().getName().indexOf(queryText) != -1) {
				sr.matches.put(n.getId(), new Match(n, 1,
						hasContentClassQueryField));
			}
		}
		return sr;
	}

	public SearchResult searchById(String queryText) {
		SearchResult sr = new SearchResult();
		int id = Integer.parseInt(queryText);
		DependencyNode n = model.getNodesById().get(id);
		if (n != null) {
			sr.matches.put(n.getId(), new Match(n, 10, editorIdQueryField));
		} else {
			logger.warn("No nodes with editor-id {}", queryText);
		}
		return sr;
	}

	/**
	 * Query the index. The fields
	 * "eid", "is" and "has" are interpreted as follows:
	 * <ul>
	 * <li>eid - exact editor-id match
	 * <li>is - node class-name match
	 * <li>has - node contents class-name match
	 * </ul>
	 * @param field field that is being searched
	 * @param queryText contents of the query
	 * @param quick 
	 * @return an object with the results of the search
	 */
	public SearchResult search(String field, String queryText, boolean quick) {

		// Short-circuited queries
		if (field.equals(isClassQueryField)) {
			return searchByClass(queryText);
		} else if (field.equals(editorIdQueryField)) {
			return searchById(queryText);
		} else if (field.equals(hasContentClassQueryField)) {
			return searchByContentClass(queryText);
		}

		// normal queries
		try {
			IndexReader reader = IndexReader.open(searchIndex);
			Query query = (field.isEmpty()) ? getQueryAllParser().parse(
					queryText) : new QueryParser(Version.LUCENE_35, field,
					searchAnalyzer).parse(queryText);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					MAX_SEARCH_HITS, true);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			SearchResult sr = new SearchResult(searcher, query, quick, hits,
					model.getNodesById());
			return sr;
		} catch (Exception e) {
			logger.warn("Error parsing or looking up query '{}' in index",
					queryText, e);
		}
		return new SearchResult();
	}

	private class UpdatePropertiesVisitor implements ModelVisitor {
		private Object toUpdate;

		private UpdatePropertiesVisitor(Object toUpdate) {
			this.toUpdate = toUpdate;
		}

		@Override
		public boolean visitObject(Object target, Object source,
				String sourceName) {
			// not interested in visiting nodes, as these are indexed separately
			return target == toUpdate;
		}

		@Override
		public void visitProperty(Object target, String propertyName,
				String textValue) {
			logger.info("Visiting property for update: '{}' :: '{}' = '{}'",
					new Object[] { target, propertyName, textValue });
			DependencyNode targetNode = (DependencyNode) target;
			model.getNodeIndex().addProperty(targetNode, propertyName,
					textValue, true);
		}
	}
}
