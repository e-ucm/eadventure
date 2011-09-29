package es.eucm.eadventure.engine.home.repository.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import android.os.Handler;
import es.eucm.eadventure.engine.home.repository.connection.parser.RepositoryDataHandler;
import es.eucm.eadventure.engine.home.repository.database.RepositoryDatabase;
import es.eucm.eadventure.engine.home.repository.handler.ProgressNotifier;
import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;
import es.eucm.eadventure.engine.home.utils.directory.Paths;

/**
 * A thread in charge of updating the repository database
 * 
 * @author Roberto Tornero
 */
public class UpdateDatabaseThread extends Thread {

	/**
	 * Location of the repository xml file on the server
	 */
	private static final String REPO_XML_FULLPATH = Paths.repository.DEFAULT_PATH + Paths.repository.SOURCE_XML;
	/**
	 * Location of the repository xml file on the local storage
	 */
	private static final String LOCAL_REPO_XML = Paths.eaddirectory.ROOT_PATH + Paths.repository.SOURCE_XML;
	/**
	 * A handler to send messages to when updating
	 */
	private Handler handler;
	/**
	 * The database to update
	 */
	private RepositoryDatabase rd;
	/**
	 * Notifies the progress of the update
	 */
	private ProgressNotifier pn;

	/**
	 * Constructor
	 */
	public UpdateDatabaseThread(Handler ha, RepositoryDatabase rd) {

		this.handler = ha;
		this.rd = rd;		
		this.pn = new ProgressNotifier(handler);
	}

	/**
	 * Starts the parsing to update the database
	 */
	@Override
	public void run() {

		try {
			downloadXML();
			parseXML();
			pn.notifyUpdateFinished("");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Downloads a new repository xml file
	 * @throws IOException
	 */
	private void downloadXML() throws IOException {

		File f = new File(LOCAL_REPO_XML);

		if (f != null)
			f.delete();

		RepoResourceHandler.downloadFile(REPO_XML_FULLPATH, Paths.eaddirectory.ROOT_PATH , Paths.repository.SOURCE_XML , pn);

	}

	/**
	 * Uses a SAX parser to update the database
	 */
	private void parseXML() {

		try {
			FileInputStream fIn = new FileInputStream(LOCAL_REPO_XML);

			if (fIn !=null) {

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				RepositoryDataHandler rsaxh = new RepositoryDataHandler(rd,pn);
				saxParser.parse(fIn, rsaxh);

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
