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

package ead.engine.home.repository.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import android.os.Handler;
import ead.engine.home.repository.connection.parser.RepositoryDataHandler;
import ead.engine.home.repository.database.RepositoryDatabase;
import ead.engine.home.repository.handler.ProgressNotifier;
import ead.engine.home.repository.handler.RepoResourceHandler;
import ead.engine.home.utils.directory.Paths;

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
