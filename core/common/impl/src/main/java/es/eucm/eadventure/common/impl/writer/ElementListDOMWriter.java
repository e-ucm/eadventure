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

package es.eucm.eadventure.common.impl.writer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;

public class ElementListDOMWriter extends DOMWriter<EAdElementList<?>> {

	private static final Logger logger = Logger.getLogger("ElementListDOMWriter");
	
	private String id;
	
	public ElementListDOMWriter(String id) {
		this.id = id;
	}

	@Override
	public Node buildNode(EAdElementList<?> list) {
        try {
        	initilizeDOMWriter();

        	node = doc.createElement( "list" );
        	node.setAttribute("name", id);

    		for (Object o : list) {
    			if (o instanceof EAdElement) {
	    			EAdElement newElement = (EAdElement) o;
					if (mappedElement.contains(newElement)) {
						Element newNode = doc.createElement("element");
						newNode.setAttribute("uniqueId", elementMap.get(newElement));
						newNode.setTextContent(elementMap.get(newElement));
						node.appendChild(newNode);
					} else {
		    			ElementDOMWriter domWriter = new ElementDOMWriter();
		    			Node newNode = domWriter.buildNode(newElement);
		    			doc.adoptNode(newNode);
		    			node.appendChild(newNode);
					}
    			} else {
					Element newNode = doc.createElement("entry");
					newNode.setTextContent(o.toString());
					node.appendChild(newNode);
    			}
    		}

        }
        catch( ParserConfigurationException e ) {
        	logger.log(Level.SEVERE, "Error writing element " + list, e);
        	return null;
        } catch (IllegalArgumentException e) {
        	logger.log(Level.SEVERE, "Illegal argument " + list, e);
		} 

        return node;
	}
	
	
}