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
 *          For more debug please visit:  <http://e-adventure.e-ucm.es> or
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
import ead.editor.Log4jConfig;
import ead.editor.Log4jConfig.Slf4jLevel;
import ead.editor.model.visitor.ModelVisitor;
import ead.editor.model.visitor.ModelVisitorDriver;
import ead.engine.core.platform.module.DesktopAssetHandlerModule;
import ead.engine.core.platform.module.DesktopModule;
import ead.engine.core.platform.modules.BasicGameModule;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.FieldOption;
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
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;
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
public class EditorModel extends ListenableDirectedGraph<EditorNode, EditorEdge> implements ModelVisitor {

    private static final Logger logger = LoggerFactory.getLogger("EditorModel");
    private int lastNodeId = 0;
    private EAdventure1XImporter importer;
    /**
     * Quick reference for node retrieval
     */
    private TreeMap<Integer, EditorNode> nodesById;
    /**
     * Contents do not guarantee "unique IDs"
     */
    private HashMap<Object, EditorNode> nodesByContent;
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
     * Constructor (with importer)
     *
     * @param importer
     */
    @Inject
    public EditorModel(EAdventure1XImporter importer) {
        super(EditorEdge.class);
        this.importer = importer;

        this.nodesById = new TreeMap<Integer, EditorNode>();
        this.nodesByContent = new HashMap<Object, EditorNode>();
        initSearchIndex();
    }

    public void loadFromFile(File f) {
        logger.info("Loading editor model from '{}'...", f);
        EAdAdventureModel m = importer.importGame(f.getAbsolutePath(), "/tmp/imported");
        logger.info("Model loaded; building graph...");
        ModelVisitorDriver driver = new ModelVisitorDriver();
        driver.visit(m, this);
        firstIndexUpdate();
        logger.info("Editor model loaded: {} nodes, {} edges",
                new Object[] {vertexSet().size(), edgeSet().size()});
    }

    private int generateId() {
        return lastNodeId++;
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
                : new EditorNode(generateId(), targetContent);

        if (!alreadyKnown) {
            addVertex(target);
        }

        if (source != null) {
            addEdge(source, target, new EditorEdge(type));
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
     * Configure Lucene indexing
     */
    private void initSearchIndex() {
        try {
            searchIndex = new RAMDirectory();
            // use a very simple analyzer; no fancy stopwords, stemming, ...
            searchAnalyzer = new WhitespaceAnalyzer(Version.LUCENE_35);
            IndexWriterConfig config = new IndexWriterConfig(
                    Version.LUCENE_35, searchAnalyzer);
            indexWriter = new IndexWriter(searchIndex, config);
        } catch (Exception e) {
            logger.error("Could not initialize search index (?)", e);
        }
    }

    // -- ModelVisitor interface
    
    @Override
    public boolean visitObject(Object target, Object source, String sourceName) {
        logger.debug("Visiting object: '{}'--['{}']-->'{}'", 
                new Object[]{ source, sourceName, target });

        // source is null for root node
        EditorNode sourceNode = (source != null) ? 
                nodesByContent.get(source) : null;        
        EditorNode e = addNode(sourceNode, sourceName, target);                
        
        if (e != null) {
            e.getDoc().add(new Field("editor-id", "" + e.getId(), Store.YES, Index.NO));
            nodesByContent.put(target, e);
            return true;
        } else {
            // already exists in graph; in this case, do not drill deeper
            return false;
        }
    }

    @Override
    public void visitProperty(Object target, String propertyName, String textValue) {
        logger.debug("Visiting property: '{}' :: '{}' = '{}'", 
                new Object[]{ target, propertyName, textValue });
        EditorNode e = nodesByContent.get(target);
        e.getDoc().add(new Field(propertyName, textValue, Store.YES, Index.ANALYZED));
    }

    // -- Search-related
    
    /**
     * Index an EditorNode for later search
     */
    private void firstIndexUpdate() {
        for (EditorNode e : vertexSet()) {
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
                        reader.getFieldNames(FieldOption.INDEXED));
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
    public List<EditorNode> searchAll(String queryText) {

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
                String nodeId = searcher.doc(hit.doc).get("editor-id");
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
    public List<EditorNode> search(String field, String queryText) {

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
                String nodeId = searcher.doc(hit.doc).get("editor-id");
                nodes.add(nodesById.get(Integer.parseInt(nodeId)));
            }
            searcher.close();
        } catch (Exception e) {
            logger.error("Error parsing or looking up query '{}' in index",
                    queryText, e);
        }

        return nodes;
    }    
    
    private class IdProvider implements VertexNameProvider<EditorNode>, EdgeNameProvider<EditorEdge> {
        @Override
        public String getVertexName(EditorNode v) {
            return ""+v.getId();
        }

        @Override
        public String getEdgeName(EditorEdge e) {
            return "";
        }        
    }
    
    private class LabelProvider implements VertexNameProvider<EditorNode>, EdgeNameProvider<EditorEdge> {
        @Override
        public String getVertexName(EditorNode v) {
            return v.getContent().getClass().getSimpleName();
        }

        @Override
        public String getEdgeName(EditorEdge e) {
            return e.getType();
        }        
    }      
    
    /**
     * Export the graph to GML
     */
    public void exportGraph(File targetFile) {        
        FileWriter fw = null;
        try {
            fw = new FileWriter(targetFile);
            IdProvider idp = new IdProvider();
            LabelProvider ldp = new LabelProvider();
            
//            GmlExporter<EditorNode, EditorEdge> exporter 
//                    = new GmlExporter<EditorNode, EditorEdge>(idp, idp, idp, idp);            
//            DOTExporter<EditorNode, EditorEdge> exporter 
//                    = new DOTExporter<EditorNode, EditorEdge>(idp, idp, idp);
            GraphMLExporter<EditorNode, EditorEdge> exporter 
                    = new GraphMLExporter<EditorNode, EditorEdge>(idp, ldp, idp, ldp); 
            exporter.export(fw, this);
        } catch (Exception e) {
            logger.warn("Error during GML export to {}", targetFile, e);
        } finally {
            try {
                if (fw != null) fw.close();
            } catch (Exception e) {
                logger.error("Could not close GML writer", e);
            }                    
        }
    }
    
    private void testSearch() {
        //for (EditorNode e : search("id", "elem*")) {
        for (EditorNode e : searchAll("disp_x")) {
            logger.info("found: " + e.getId() + " " 
                    + e.getContent().getClass().getSimpleName() + " " 
                    + e.getContent() + " :: " + 
                        (e.getContent() instanceof EAdElement ? ((EAdElement)e.getContent()).getId() : "??"));
        }        
    }

    public static void main(String[] args) {
        
        Log4jConfig.configForConsole(Slf4jLevel.Fatal, new Object[] {
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
        model.loadFromFile(f);
        
        model.testSearch();
        //model.exportGraph(new File("/tmp/exported.graphml"));
    }
}
