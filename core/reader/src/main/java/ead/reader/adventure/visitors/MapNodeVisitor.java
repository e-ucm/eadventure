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

package ead.reader.adventure.visitors;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ead.common.model.EAdElement;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.reader.adventure.DOMTags;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	protected static final Logger logger = LoggerFactory
			.getLogger("MapNodeVisitor");	
	
	@SuppressWarnings("unchecked")
	@Override
	public void visit(XMLNode node, ReflectionField field, Object parent,
			Class<?> listClass, NodeVisitorListener listener) {
		XMLNodeList nl = node.getChildNodes();

		EAdMap<Object, Object> map = null;

		if (field == null || parent == null) {
			map = createNewMap(node);
			logger.debug("Created new {}-to-{} map", 
					new String[] { 
						map.getKeyClass().getSimpleName(), 
						map.getValueClass().getSimpleName() });
		} else {
			map = (EAdMap<Object, Object>) field.getFieldValue(parent);
			String pid = ((parent instanceof EAdElement) ? 
					((EAdElement)parent).getId() : 
					parent.getClass().getSimpleName() + "@" + parent.hashCode());			
			if (logger.isDebugEnabled()) {
				logger.debug("Using prior map {} at {}.{}",
						new String[] {
							map.getClass().getSimpleName() + "@" + map.hashCode(), 
							pid, field.getName()
						});
			}
		}
		logger.debug("Map listener is a {}", listener.getClass().toString());

		String type;
		for (int i = 0, cnt = nl.getLength(); i < cnt - 1; i += 2) {
			try {
				type = nl.item(i).getNodeName();
				MapNodeVisitorListener mapListener = 
						new MapNodeVisitorListener(map);
				// sets the entry key
				VisitorFactory.getVisitor(type).visit(nl.item(i), null, null,
						map.getKeyClass(), mapListener);
				type = nl.item(i + 1).getNodeName();
				// sets the entry value, commits to parent map
				VisitorFactory.getVisitor(type).visit(nl.item(i + 1), null,
						null, map.getValueClass(), mapListener);
			} catch (Exception e) {
				logger.error("Could not iterate through map", e);
			}
		}
		listener.elementRead(map);
	}

	private EAdMap<Object, Object> createNewMap(XMLNode node) {
		
		String keyClazz = node.getAttributes().getValue(
				DOMTags.KEY_CLASS_AT);
		keyClazz = translateClass(keyClazz);
		ReflectionClass<?> keyClassType = ReflectionClassLoader
				.getReflectionClass(keyClazz);

		String valueClazz = node.getAttributes().getValue(
				DOMTags.VALUE_CLASS_AT);
		valueClazz = translateClass(valueClazz);
		ReflectionClass<?> valueClassType = ReflectionClassLoader
				.getReflectionClass(valueClazz);

		return new EAdMapImpl<Object, Object>(
				keyClassType.getType(),
				valueClassType.getType());
	}

	@Override
	public String getNodeType() {
		return DOMTags.MAP_TAG;
	}

	/**
	 * The first time that elementRead is called, an internal key is set
	 * Further elementRead calls update that key's value in the original map
	 */
	public static class MapNodeVisitorListener implements NodeVisitorListener {

		private EAdMap<Object, Object> map;

		private Object key;

		public MapNodeVisitorListener(EAdMap<Object, Object> map) {
			this.map = map;
		}

		@Override
		public void elementRead(Object element) {
			if (key == null) {
				key = element;
			} else {
				if (logger.isDebugEnabled()) {
					String k = ((key instanceof EAdElement) ? 
							((EAdElement)key).getId() : 
							key.getClass().getSimpleName() + "@" + key.hashCode());
					String e = ((element instanceof EAdElement) ? 
							((EAdElement)element).getId() : 
							element.getClass().getSimpleName() + "@" + element.hashCode());
					
					logger.debug("{}[{}] <-- {}", new String[] {
						map.getClass().getSimpleName() + "@" + map.hashCode(), 
						k, e});					
				}
				// for editor nodes, IDs are important; however, they are ignored in VarDef equals-comparisons
				map.remove(key);				
				map.put(key, element);
			}
		}
	}
}
