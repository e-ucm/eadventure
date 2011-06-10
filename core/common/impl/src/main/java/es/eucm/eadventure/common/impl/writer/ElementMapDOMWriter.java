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

import javax.xml.parsers.ParserConfigurationException;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.EAdMap;

public class ElementMapDOMWriter extends DOMWriter<EAdMap<?, ?>> {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger.getLogger("ElementMapDOMWriter");
	
	/**
	 * The id of the element map
	 */
	private String id;
	
	public ElementMapDOMWriter(String id) {
		this.id = id;
	}

	@Override
	public Node buildNode(EAdMap<?, ?> map) {
        try {
        	initilizeDOMWriter();

        	node = doc.createElement( "map" );
        	node.setAttribute("name", id);

    		for (Object o : map.keySet()) {
    			Element entryNode = doc.createElement( "entry" );
    			entryNode.setAttribute("key", o.toString());
    			if (map.get(o) instanceof EAdElementList) {
					EAdElementList<?> list = (EAdElementList<?>) map.get(o);
					ElementListDOMWriter listWriter = new ElementListDOMWriter(o.toString());
					Node listNode = listWriter.buildNode(list);
					doc.adoptNode(listNode);
					entryNode.appendChild(listNode);
    			} else if (map.get(o) instanceof EAdElement) {
    				EAdElement newElement = (EAdElement) map.get(o);
    				if (mappedElement.contains(newElement)) {
    					logger.info("Added element id:" + elementMap.get(newElement) + "; element:" + newElement);
    					entryNode.setAttribute("uniqueId", elementMap.get(newElement));
    					entryNode.setTextContent(elementMap.get(newElement));
    				} else {
    					ElementDOMWriter domWriter = new ElementDOMWriter();
    					Node newNode = domWriter.buildNode((EAdElement) map.get(o));
    					doc.adoptNode(newNode);
    					entryNode.appendChild(newNode);
    				}
    			} else {
    				entryNode.setTextContent(map.get(o).toString());
    			}
    			
    			node.appendChild(entryNode);
    		}


        }
        catch( ParserConfigurationException e ) {
        	logger.log(Level.SEVERE, "Error writing element " + map, e);
        	return null;
        } catch (IllegalArgumentException e) {
        	logger.log(Level.SEVERE, "Illegal argument " + map, e);
		} 

        return node;
	}
	
}
