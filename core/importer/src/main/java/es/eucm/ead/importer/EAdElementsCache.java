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

package es.eucm.ead.importer;

import com.google.inject.Singleton;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.operations.ElementField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class EAdElementsCache {

	static private Logger logger = LoggerFactory
			.getLogger(EAdElementsCache.class);

	private Map<String, BasicElement> elements;

	private Map<String, Integer> references;

	private Map<String, Map<String, ElementField>> fields;

	public EAdElementsCache() {
		elements = new HashMap<String, BasicElement>();
		fields = new HashMap<String, Map<String, ElementField>>();
		references = new HashMap<String, Integer>();
	}

	public void put(BasicElement element) {
		elements.put(element.getId(), element);
	}

	public BasicElement get(String id) {
		BasicElement element = elements.get(id);
		if (element == null) {
			logger.warn("No element for id {}", id);
		}
		return element;
	}

	public Integer newReference(String id) {
		Integer ref = references.get(id);
		if (ref == null) {
			ref = 0;
		} else {
			ref++;
		}
		references.put(id, ref);
		return ref;
	}

	public <T> ElementField getField(String elementId, String var) {
		return getField(elements.get(elementId), var);
	}

	@SuppressWarnings("unchecked")
	public <T> ElementField getField(BasicElement element, String var) {
		if (element != null && element.getId() != null) {

			Map<String, ElementField> fieldsMap = fields.get(element.getId());
			if (fieldsMap == null) {
				fieldsMap = new HashMap<String, ElementField>();
				fields.put(element.getId(), fieldsMap);
			}

			ElementField field = (ElementField) fieldsMap.get(var);
			if (field == null) {
				field = new ElementField(element, var);
				fieldsMap.put(var, field);
			}
			return field;
		} else {
			return new ElementField(element, var);
		}
	}

	public void clear() {
		elements.clear();
		fields.clear();
	}
}
