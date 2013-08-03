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

package ead.converter;

import com.google.inject.Singleton;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.params.variables.EAdVarDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class EAdElementsCache {

	private static final Logger logger = LoggerFactory
			.getLogger("ElementsCache");

	private Map<String, EAdElement> elements;

	private Map<String, Map<EAdVarDef<?>, EAdField<?>>> fields;

	public EAdElementsCache() {
		elements = new HashMap<String, EAdElement>();
		fields = new HashMap<String, Map<EAdVarDef<?>, EAdField<?>>>();
	}

	public void put(EAdElement element) {
		elements.put(element.getId(), element);
	}

	public EAdElement get(String id) {
		EAdElement element = elements.get(id);
		if (element == null) {
			logger.warn("No element for id {}", id);
		}
		return element;
	}

	public <T> EAdField<T> getField(String elementId, EAdVarDef<T> var) {
		return getField(elements.get(elementId), var);
	}

	@SuppressWarnings("unchecked")
	public <T> EAdField<T> getField(EAdElement element, EAdVarDef<T> var) {
		if (element != null && element.getId() != null) {

			Map<EAdVarDef<?>, EAdField<?>> fieldsMap = fields.get(element
					.getId());
			if (fieldsMap == null) {
				fieldsMap = new HashMap<EAdVarDef<?>, EAdField<?>>();
				fields.put(element.getId(), fieldsMap);
			}

			EAdField<T> field = (EAdField<T>) fieldsMap.get(var);
			if (field == null) {
				field = new BasicField<T>(element, var);
				fieldsMap.put(var, field);
			}
			return field;
		} else {
			return new BasicField<T>(element, var);
		}
	}

	public void clear() {
		elements.clear();
		fields.clear();
	}
}
