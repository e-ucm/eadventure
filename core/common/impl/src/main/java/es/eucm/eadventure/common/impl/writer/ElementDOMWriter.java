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

import es.eucm.eadventure.common.interfaces.EAdRuntimeException;
import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.resources.EAdResources;

public class ElementDOMWriter extends DOMWriter<EAdElement> {

	public static final Logger logger = Logger
			.getLogger("ElementDOMWriter");

	@Override
	public Node buildNode(EAdElement element) {
		try {
			initilizeDOMWriter();

			node = doc.createElement("element");
			node.setAttribute("id", element.getId());
			if (!elementMap.containsKey(element))
				elementMap.put(element, "" + elementMap.keySet().size());
			mappedElement.add(element);
			node.setAttribute("uniqueId", elementMap.get(element));

			Class<?> clazz = element.getClass();
			Element annotation = null;
			
			while (annotation == null && clazz != null) {
				annotation = clazz.getAnnotation(Element.class);
				clazz = clazz.getSuperclass();
			}

			if (annotation == null) {
				logger.log(Level.SEVERE, "No Element annotation in class "
						+ element.getClass());
				throw new EAdRuntimeException("No Element annotation in class "
						+ element.getClass());
			}
			node.setAttribute("type", annotation.detailed().getCanonicalName());
			node.setAttribute("class", annotation.runtime().getCanonicalName());

			ParamDOMWriter paramDOMWriter = new ParamDOMWriter(element);

			clazz = element.getClass();

			while (clazz != null) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					boolean accessible = field.isAccessible();
					field.setAccessible(true);

					if (field.get(element) instanceof EAdList) {
						EAdList<?> list = (EAdList<?>) field
								.get(element);
						ElementListDOMWriter listWriter = new ElementListDOMWriter(field.getName());
						Node listNode = listWriter.buildNode(list);
						doc.adoptNode(listNode);
						node.appendChild(listNode);
					} else if ( field.get(element) instanceof EAdMap ){
						EAdMap<?, ?> map = (EAdMap<?, ?>) field.get(element);
						ElementMapDOMWriter mapWriter = new ElementMapDOMWriter( field.getName() );
						Node mapNode = mapWriter.buildNode(map);
						doc.adoptNode(mapNode);
						node.appendChild(mapNode);
					} else if ( field.get(element) instanceof EAdResources ) {
						ResourcesDOMWriter resourcesWriter = new ResourcesDOMWriter( );
						Node resourcesNode = resourcesWriter.buildNode((EAdResources) field.get(element));
						doc.adoptNode(resourcesNode);
						node.appendChild(resourcesNode);
					} else if (field.isAnnotationPresent(Param.class)) {
						if (field.get(element) != null) {
							Node paramNode = paramDOMWriter.buildNode(field);
							if (paramNode != null) {
								doc.adoptNode(paramNode);
								node.appendChild(paramNode);
							}
						}
					}

					field.setAccessible(accessible);
				}
				
				clazz = clazz.getSuperclass();
			}

		} catch (ParserConfigurationException e) {
			logger.log(Level.SEVERE, "Error writing element " + element, e);
			return null;
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return node;
	}

}
