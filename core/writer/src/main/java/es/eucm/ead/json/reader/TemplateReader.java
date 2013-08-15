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

package es.eucm.ead.json.reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.StringMap;

@SuppressWarnings( { "unchecked", "rawtypes" })
public class TemplateReader {

	private static final Logger logger = LoggerFactory
			.getLogger("TemplateReader");
	private Map<String, StringMap<Object>> templates;
	private StringMap<Object> templateValues;

	public TemplateReader() {
		templates = new HashMap<String, StringMap<Object>>();
		templateValues = new StringMap<Object>();
	}

	public void addTemplate(StringMap<Object> template) {
		templates.put(template.get("templateId").toString(), template);
	}

	public void applyTemplates(Object object) {
		templateValues.clear();
		applyTemplates(object, templateValues);
		applyTemplateValues(object, templateValues);
	}

	/**
	 * Extends all templates inside an object
	 * 
	 * @param o
	 * @param templateValues
	 */
	public void applyTemplates(Object o, StringMap<Object> templateValues) {

		// It it is an object
		if (o instanceof StringMap) {
			StringMap<Object> object = (StringMap<Object>) o;
			// Look for inheritance
			String inherits = (String) object.get("inherits");
			// If inherits a template
			if (inherits != null) {
				StringMap<Object> template = getTemplate(inherits);
				if (template == null) {
					logger.error("No template for {}",
							new Object[] { inherits });
				}
				// We add the 'templated' values
				StringMap<Object> values = (StringMap<Object>) object
						.get("template");
				if (values != null)
					for (String key : values.keySet()) {
						if (!templateValues.containsKey(key)) {
							templateValues.put(key, values.get(key));
						}
					}
				// We remove some attributes, they are no longer needed
				object.remove("inherits");
				object.remove("template");

				// We add attributes from the template
				for (String key : template.keySet()) {
					// If the object doesn't contain a value for the key
					if (!object.containsKey(key)) {
						// We set the value from the template
						object.put(key, template.get(key));
					}
				}

			}
			// Maybe some child attribute inherits from some template
			for (Object v : object.values()) {
				applyTemplates(v, templateValues);
			}
		}
		// If it is a collection
		else if (o instanceof Collection) {
			Collection c = (Collection) o;
			for (Object v : c) {
				applyTemplates(v, templateValues);
			}
		}
	}

	/**
	 * Substitutes all template values in an object
	 * 
	 * @param object
	 * @param templateValues
	 */
	private void applyTemplateValues(Object o, StringMap<Object> templateValues) {
		// If it is an object
		if (o instanceof StringMap) {
			StringMap<Object> object = (StringMap<Object>) o;
			// Look for template marks in the attributes
			for (String key : object.keySet()) {
				Object value = object.get(key);
				// It it has a mark
				if (value instanceof String && !"".equals(value)
						&& ((String) value).charAt(0) == '?') {
					// Substitute the value
					object.put(key, templateValues.get(((String) value)
							.substring(1)));
				} else {
					// Else, apply to child
					applyTemplateValues(value, templateValues);
				}
			}
		}
		// If it is a collection
		else if (o instanceof Collection) {
			Collection c = (Collection) o;
			// Apply to all elements in the colleciton
			for (Object object : c) {
				applyTemplateValues(object, templateValues);
			}
		}

	}

	public StringMap<Object> getTemplate(String templateId) {
		StringMap<Object> template = templates.get(templateId);
		StringMap<Object> templateCopy = (StringMap<Object>) cloneValue(template);
		return templateCopy;
	}

	private Object cloneValue(Object value) {
		if (value instanceof StringMap) {
			StringMap<Object> object = (StringMap<Object>) value;
			StringMap<Object> copy = new StringMap<Object>();
			for (Entry<String, Object> e : object.entrySet()) {
				Object clone = cloneValue(e.getValue());
				copy.put(e.getKey(), clone);
			}
			return copy;
		} else if (value instanceof Collection) {
			Collection c = (Collection) value;
			Collection copy = new ArrayList();
			for (Object v : c) {
				copy.add(cloneValue(v));
			}
			return copy;
		}
		return value;
	}
}
