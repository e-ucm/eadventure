/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.model;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ead.common.importer.ImporterConfigurationModule;
import ead.common.model.EAdElement;
import ead.editor.Log4jConfig;
import ead.engine.core.platform.module.DesktopAssetHandlerModule;
import ead.engine.core.platform.module.DesktopModule;
import ead.engine.core.platform.modules.BasicGameModule;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfreire
 */
public class EditorModelTest {

    private static final Logger logger = LoggerFactory.getLogger("EditorModelTest");
	
    private EditorModel model;
	
    public EditorModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {				
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info, new Object[]{
			"ModelVisitorDriver", Log4jConfig.Slf4jLevel.Info,
			"EditorModel", Log4jConfig.Slf4jLevel.Debug
		});		
		
        Injector injector = Guice.createInjector(
                new ImporterConfigurationModule(),
                new BasicGameModule(),
                new DesktopModule(),
                new DesktopAssetHandlerModule());
        model = injector.getInstance(EditorModel.class);		
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of loadFromImportFile method, of class EditorModel.
     */
    @Test
    public void testLoadFromImportFile() {
        System.out.println("loadFromImportFile");       
    }

    /**
     * Test of save method, of class EditorModel.
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
    }

    /**
     * Test of load method, of class EditorModel.
     */
    @Test
    public void testLoad() throws Exception {
        System.out.println("load");
    }

    /**
	 * Tests indexing and searching
	 */	
    public void simpleSearch() {
		String s = "disp_x";
        logger.info("Now searching for '"+s+"' in all fields, all nodes...");
		for (EditorNode e : model.searchAll(s)) {
            logger.info("found: " + e.getId() + " "
                    + e.getContent().getClass().getSimpleName() + " "
                    + e.getContent() + " :: "
                    + (e.getContent() instanceof EAdElement ? ((EAdElement) e.getContent()).getId() : "??"));
        }
    }

    public static void main(String[] args) {
		EditorModelTest emt = new EditorModelTest();
		emt.setUp();

		logger.info("Starting test run...");
        File f = new File("/home/mfreire/code/e-ucm/e-adventure-1.x/games/PrimerosAuxiliosGame.ead");
		emt.model.loadFromImportFile(f, new File("/tmp/imported"));
		
		emt.simpleSearch();
        //emt.model.exportGraph(new File("/tmp/exported.graphml"));
    }
}
