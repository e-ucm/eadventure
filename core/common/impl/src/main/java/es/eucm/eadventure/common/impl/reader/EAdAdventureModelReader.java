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

package es.eucm.eadventure.common.impl.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Reader;
import es.eucm.eadventure.common.impl.reader.subparsers.AdventureHandler;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;

/**
 * The reader for the XML representation of the model
 */
@Deprecated
public class EAdAdventureModelReader implements Reader<EAdAdventureModel> {
	
	private static final Logger logger = Logger.getLogger("EAdReader");
	
	/**
	 * The SAX handler to read the model from the XML file.
	 */
	private AdventureHandler adventureHandler;
	
	@Inject
	public EAdAdventureModelReader(AdventureHandler adventureHandler) {
		this.adventureHandler = adventureHandler;
	}
	
	@Override
	public EAdAdventureModel read(URI fileURI) {
		try {
			File file = new File(fileURI);
			FileInputStream fileInputStream = new FileInputStream(file);
			EAdAdventureModel data = read(fileInputStream);
			return data;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public EAdAdventureModel read(InputStream inputStream) {
		EAdAdventureModel data = null;
		try {
            SAXParserFactory factory = SAXParserFactory.newInstance( );
            factory.setValidating( false );
            SAXParser saxParser = factory.newSAXParser( );
            
            saxParser.parse( inputStream, adventureHandler );
			data = adventureHandler.getAdventureModel();
			return data;
			
		} catch( ParserConfigurationException e ) {
			logger.log(Level.SEVERE, e.getMessage(), e);
        }
        catch( SAXException e ) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
        }
        catch( IOException e ) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
        }
        catch( IllegalArgumentException e ) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
        }		
        return null;
	}


}
