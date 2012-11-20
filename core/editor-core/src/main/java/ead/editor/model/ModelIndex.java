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

import ead.editor.model.nodes.DependencyNode;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class ModelIndex {

	private static final Logger logger = LoggerFactory.getLogger("ModelIndex");
	public static final String editorIdFieldName = "editor-id";
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
	 * Configure Lucene indexing
	 */
	public ModelIndex() {
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
	public void addProperty(DependencyNode e, String field, String value,
			boolean searchable) {

		e.getDoc().add(
				new Field(field, value, Store.YES, searchable ? Index.ANALYZED
						: Index.NO));
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
	 * Represents query results
	 */
	public static class SearchResult {

		private ArrayList<DependencyNode> matches = new ArrayList<DependencyNode>();
		private TreeMap<Integer, ArrayList<String>> fieldMatches = new TreeMap<Integer, ArrayList<String>>();

		private static final Pattern fieldMatchPattern = Pattern
				.compile("fieldWeight[(]([^:]+):");

		public SearchResult() {
			// used for "empty" searches: no results
		}

		public SearchResult(IndexSearcher searcher, Query query,
				ScoreDoc[] hits, Map<Integer, DependencyNode> nodesById)
				throws IOException {

			try {
				for (ScoreDoc hit : hits) {
					String nodeId;
					nodeId = searcher.doc(hit.doc).get(editorIdFieldName);
					logger.debug("Adding {}", nodeId);
					DependencyNode node = nodesById.get(Integer
							.parseInt(nodeId));
					matches.add(node);
					fieldMatches.put(node.getId(), new ArrayList<String>());
					fillFieldsForExplanation(searcher.explain(query, hit.doc),
							node.getId());
				}
				searcher.close();
				if (logger.isDebugEnabled()) {
					logger.debug("Found {} matches for query {}:",
							new Object[] { matches.size(), query });
					for (DependencyNode node : matches) {
						logger.debug(" * node {}", node.getId());
						if (fieldMatchesFor(node) != null) {
							logger
									.debug(
											"    {} fields match",
											new Object[] { fieldMatchesFor(node)
													.size() });
						} else {
							logger.error("No field matches for match {} !! ",
									node.getId());
						}
					}
				}
			} catch (CorruptIndexException e) {
				throw new IOException("Corrupt index", e);
			}
		}

		public final void fillFieldsForExplanation(Explanation e, int nodeId) {
			String s = e.getDescription();
			logger.debug("Reading explanation for {}: '{}'", new Object[] {
					nodeId, s });
			Matcher m = fieldMatchPattern.matcher(s);
			if (m.find()) {
				logger.debug("Adding another match to {}: {}", new Object[] {
						nodeId, m.group(1) });
				ArrayList<String> ms = fieldMatches.get(nodeId);
				ms.add(m.group(1));
			}

			// recurse
			if (e.getDetails() == null) {
				return;
			}
			for (Explanation se : e.getDetails()) {
				fillFieldsForExplanation(se, nodeId);
			}
		}

		public List<DependencyNode> getMatchedNodes() {
			return matches;
		}

		public List<String> fieldMatchesFor(DependencyNode node) {
			return fieldMatches.get(node.getId());
		}
	}

	/**
	 * Get a (sorted) list of nodes that match a query
	 */
	public List<DependencyNode> searchAll(String queryText,
			Map<Integer, DependencyNode> nodesById) {
		return searchAllDetailed(queryText, nodesById).getMatchedNodes();
	}

	/**
	 * Get a (sorted) list of nodes that match a query
	 */
	public SearchResult searchAllDetailed(String queryText,
			Map<Integer, DependencyNode> nodesById) {
		try {
			IndexReader reader = IndexReader.open(searchIndex);
			Query query = getQueryAllParser().parse(queryText);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					MAX_SEARCH_HITS, true);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			SearchResult sr = new SearchResult(searcher, query, hits, nodesById);
			return sr;
		} catch (Exception e) {
			logger.error("Error parsing or looking up query '{}' in index",
					queryText, e);
		}
		return new SearchResult();
	}

	/**
	 * Get a (sorted) list of nodes that match a query
	 */
	public List<DependencyNode> search(String field, String queryText,
			Map<Integer, DependencyNode> nodesById) {

		ArrayList<DependencyNode> nodes = new ArrayList<DependencyNode>();
		try {
			IndexReader reader = IndexReader.open(searchIndex);
			Query query = new QueryParser(Version.LUCENE_35, field,
					searchAnalyzer).parse(queryText);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					MAX_SEARCH_HITS, true);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (ScoreDoc hit : hits) {
				String nodeId = searcher.doc(hit.doc).get(editorIdFieldName);
				nodes.add(nodesById.get(Integer.parseInt(nodeId)));
			}
			searcher.close();
		} catch (Exception e) {
			logger.error("Error parsing or looking up query '{}' in index",
					queryText, e);
		}

		return nodes;
	}
}
