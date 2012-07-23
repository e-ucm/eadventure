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

package ead.reader.java;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ead.common.params.text.EAdString;

public class StringXMLHandler extends DefaultHandler {
	
	private Map<EAdString, String> strings;
	private String key;
	private StringBuffer text;
	
	public StringXMLHandler(){
		strings = new HashMap<EAdString,String>();
	}
	
	public Map<EAdString, String> getStrings(){
		return strings;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("string")){
			key = attributes.getValue("name");
			text = new StringBuffer();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if ( text != null )
			text.append( new String( ch, start, length ) );
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ( qName.equals("string")){
			String value = text.toString();
			EAdString string = new EAdString(key);
			strings.put(string, value);
		}
	}

}
