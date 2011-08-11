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

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;

/**
 * <p>DOM writer for the "param" element in the eAdventure 2 xml</p>
 *
 */
public class ParamDOMWriter extends DOMWriter<Field> {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("ParamDOMWriter");

	/**
	 * The element where to add the param
	 */
	private Object element;
	
	public ParamDOMWriter(Object element) {
		this.element = element;
		try {
			initilizeDOMWriter();
		} catch (ParserConfigurationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public Node buildNode(Field field) {
		Param param = field.getAnnotation(Param.class);
		String name = param.value();
		
    	node = doc.createElement( "param" );
    	node.setAttribute("name", name);

    	try {
			if (field.get(element) instanceof EAdElement) {
				EAdElement newElement = (EAdElement) field.get(element);
				if (mappedElement.contains(newElement)) {
					logger.info("Added element id:" + elementMap.get(newElement) + "; element:" + newElement);
					node.setAttribute("uniqueId", elementMap.get(newElement));
					node.setTextContent(elementMap.get(newElement));
				} else {
					ElementDOMWriter domWriter = new ElementDOMWriter();
					Node newNode = domWriter.buildNode((EAdElement) field.get(element));
					doc.adoptNode(newNode);
					node.appendChild(newNode);
				}
			} else if (field.get(element) instanceof EAdVar) {
				EAdVar<?> elementVar = (EAdVar<?>) field.get(element);
				String value = elementVar.toString();
				if (elementVar.getElement() != null) {
					if (!elementMap.containsKey(elementVar.getElement()))
						elementMap.put(elementVar.getElement(), ""+elementMap.keySet().size());
					value += elementMap.get(elementVar.getElement()) + ";";
				} else {
					value += ";";
				}
				node.setTextContent(value);
			} else if ( field.get(element) instanceof Class ){
				node.setTextContent(((Class<?>) field.get(element)).getName());
			} else {
				node.setTextContent(field.get(element).toString());
			}
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return node;
	}
	
	

	
}
