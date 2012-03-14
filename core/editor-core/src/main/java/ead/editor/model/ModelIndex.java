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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
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
     * Query parser
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
            IndexWriterConfig config = new IndexWriterConfig(
                    Version.LUCENE_35, searchAnalyzer);
            indexWriter = new IndexWriter(searchIndex, config);
        } catch (Exception e) {
            logger.error("Could not initialize search index (?)", e);
            throw new IllegalArgumentException("Could not initialize search index (?)", e);
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
    public void addProperty(EditorNode e, String field, String value,
            boolean searchable) {
        e.getDoc().add(new Field(field, value, Store.YES,
                searchable? Index.ANALYZED : Index.NO));
    }


    /**
     * Index an EditorNode for later search
     */
    public void firstIndexUpdate(Collection<EditorNode> nodes) {
        for (EditorNode e : nodes) {
            Document doc = e.getDoc();
            logger.trace("Writing index for {} of class {}",
                    new Object[] {e.getId(), e.getContent().getClass().getSimpleName()});
            try {
                indexWriter.addDocument(doc);
            } catch (Exception ex) {
                logger.error("Error adding search debugrmation for node {}",
                        e.getId(), ex);
            }
        }
        try {
            indexWriter.commit();
        } catch (Exception ex) {
            logger.error("Error commiting search debugrmation", ex);
        }
    }

    /**
     * Lazily create or return the query parser
     */
    private QueryParser getQueryAllParser() {

        if (queryParser == null) {
            try {
                IndexReader reader = IndexReader.open(searchIndex);

                ArrayList<String> al = new ArrayList<String>(
                        reader.getFieldNames(IndexReader.FieldOption.INDEXED));
                String[] allFields = al.toArray(new String[al.size()]);
                if (logger.isDebugEnabled()) {
                    Arrays.sort(allFields);
                    logger.debug("enumerating indexed fields");
                    for (String name : allFields) {
                        logger.debug("  indexed field: '{}'", name);
                    }
                }
                queryParser = new MultiFieldQueryParser(
                        Version.LUCENE_35, allFields, searchAnalyzer);
            } catch (IOException ioe) {
                logger.error("Error constructing query parser", ioe);
            }
        }
        return queryParser;
    }


    /**
     * Get a (sorted) list of nodes that match a query
     */
    public List<EditorNode> searchAll(String queryText, Map<Integer, EditorNode> nodesById) {

        ArrayList<EditorNode> nodes = new ArrayList<EditorNode>();
        try {
            IndexReader reader = IndexReader.open(searchIndex);
            Query query = getQueryAllParser().parse(queryText);
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

    /**
     * Get a (sorted) list of nodes that match a query
     */
    public List<EditorNode> search(String field, String queryText, Map<Integer, EditorNode> nodesById) {

        ArrayList<EditorNode> nodes = new ArrayList<EditorNode>();
        try {
            IndexReader reader = IndexReader.open(searchIndex);
            Query query = new QueryParser(
                    Version.LUCENE_35, field, searchAnalyzer).parse(queryText);
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
