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

package ead.engine.android.home.repository.connection.parser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.util.Log;
import ead.engine.android.home.repository.database.GameInfo;
import ead.engine.android.home.repository.database.RepositoryDatabase;
import ead.engine.android.home.repository.handler.ProgressNotifier;
import ead.engine.android.home.repository.handler.RepoResourceHandler;

/**
 * A Handler to parse the data retrieved from the repository xml file 
 * 
 * @author Roberto Tornero
 */
public class RepositoryDataHandler extends DefaultHandler {

	/**
	 * Current string value that is being parsed
	 */
	private StringBuffer currentString = null;
	/**
	 * The games repository database to fill
	 */
	private RepositoryDatabase repositoryInfo;
	/**
	 * A progress notifier for the parsing of the repository xml file
	 */
	private ProgressNotifier pn ;
	/**
	 * If the game has any title specified on the xml
	 */
	private boolean hasTitle = false;
	/**
	 * If the game has any icon image specified on the xml
	 */
	private boolean hasImageIcon = false;
	/**
	 * If the game has any description specified on the xml
	 */
	private boolean hasDescription = false;
	/**
	 * If the game has any url link to download from specified on the xml
	 */
	private boolean hasUrl = false;
	/**
	 * Temporal string values to store the title, description and url of the game 
	 */
	private String tit, des, url;
	/**
	 * Temporal bitmap value to store the icon of the game
	 */
	private Bitmap imgIcon ;

	/**
	 * Constructor
	 */
	public RepositoryDataHandler(RepositoryDatabase rd, ProgressNotifier pn) {

		this.currentString = new StringBuffer();
		this.repositoryInfo = rd;
		this.pn = pn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String namespaceURI, String sName, String qName,
			Attributes attrs) throws SAXException {

		if (sName.equals("title")) {
			hasTitle = true;
		}

		if (sName.equals("imageIcon")) {
			hasImageIcon = true;			
		}

		if (sName.equals("description")) {
			hasDescription = true;
		}

		if (sName.equals("url")) {
			hasUrl = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String namespaceURI, String sName, String qName)
	throws SAXException {

		Log.d("EndElement", "XMLNS : " + namespaceURI + " SNAME : " + sName
				+ " QNAME : " + qName);

		if (sName.equals("title")) {
			hasTitle = false;
		}

		if (sName.equals("imageIcon")) {
			hasImageIcon = false;
		}

		if (sName.equals("description")) {
			hasDescription = false;
		}

		if (sName.equals("url")) {
			hasUrl = false;
		}

		if (sName.equals("game")) {
			hasDescription = false;


			this.repositoryInfo.addGameInfo(new GameInfo(tit, des, url, imgIcon));
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] buf, int offset, int len) throws SAXException {

		currentString.append(new String(buf, offset, len));


		if (hasTitle) {
			tit = new String(new String(buf, offset, len));
		}

		if (hasImageIcon) {
			String im = new String(new String(buf, offset, len));
			Log.d("Characters", "Text : " + new String(buf, offset, len));

			imgIcon = RepoResourceHandler.DownloadImage(im,pn);

		}

		if (hasDescription) {
			des = new String(new String(buf, offset, len));
		}

		if (hasUrl) {
			url = new String(new String(buf, offset, len));
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(SAXParseException exception) throws SAXParseException {

		// On validation, propagate exception
		exception.printStackTrace();
		Log.d("Error", "SAXParseException");
		throw exception;
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#resolveEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId) {

		Log.d("resolveEntity", "PublicID : " + publicId + " SystemId : "
				+ systemId);

		return null;
	}

}
