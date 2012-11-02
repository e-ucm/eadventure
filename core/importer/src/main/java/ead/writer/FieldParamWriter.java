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

package ead.writer;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.w3c.dom.Element;

import ead.common.interfaces.Param;
import ead.common.model.EAdElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdAssetBundle;
import ead.common.resources.EAdResources;
import ead.reader.adventure.DOMTags;

public abstract class FieldParamWriter<T> extends DOMWriter<T> {

	public void processParams(Element node, T data) {
		DOMWriter.depthManager.levelUp();

		Class<?> clazz = data.getClass();

		String debugData = null;
		if (logger.isDebugEnabled()) {
			debugData = (data instanceof EAdElement) ?
					((EAdElement)data).getId() :
					clazz.getSimpleName() + "@" + data.hashCode();
		}
		
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				try {
					Param param = field.getAnnotation(Param.class);
					if (param != null) {
						PropertyDescriptor pd = getPropertyDescriptor(data.getClass(), field.getName());
						if (pd == null) {
							logger.error("Missing descriptor for {} in {}: "
									+ "Probably needs get or set method on this field",
                                    field.getName(), data.getClass());
							error = true;
                        }
                        Method method = pd.getReadMethod();
						if (method == null) {
							logger.error("Missing read-method for {} in {}",
                                    field.getName(), data.getClass());
							error = true;
                        }
                        Object o = method.invoke(data);

						if (!isEmpty(o)) {
							logger.debug("not empty, so will generate param for {}.{}", 
									new Object[] {debugData, field.getName()});								
							Element newNode = super.initNode(o, null);
							if (!DOMWriter.USE_DEFAULT_VALUES || !isDefault(newNode, o, param.defaultValue())) {
								newNode.setAttribute(DOMTags.PARAM_AT, param.value());								
								doc.adoptNode(newNode);
								node.appendChild(newNode);
								logger.debug("param appended: {}.{}", 
										new Object[] {debugData, field.getName()});								
							} else {
								logger.debug("param created but not appended: {}.{} [{} / {} == {}]", 
										new Object[] {debugData, field.getName(), 
											!DOMWriter.USE_DEFAULT_VALUES, newNode.getTextContent(), param.defaultValue()});								
							}
						} else {
							logger.debug("ignored empty param {}.{}", 
									new Object[] {debugData, field.getName()});
						}
					}
				} catch (Exception e) {
					logger.error("Caught exception while iterating fields", e);
				}
			}
			clazz = clazz.getSuperclass();
		}

		DOMWriter.depthManager.levelDown();
	}


	/**
	 * Utility method to find a property descriptor for a single property
	 *
	 * @param c
	 * @param fieldName
	 * @return
	 */
	private static PropertyDescriptor getPropertyDescriptor(Class<?> c, String fieldName) {
		try {
			for (PropertyDescriptor pd :
				Introspector.getBeanInfo(c).getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("Could not find getters or setters for field "
					+ fieldName + " in class " + c.getCanonicalName());
		}
		return null;
	}

	/**
	 * Checks whether newNode contains the same default value as 'd'.
	 * @param newNode 
	 * @param o object from which newNode was generated
	 * @param d default value to compare against
	 * @return true if o is default, false otherwise
	 */
	public boolean isDefault(Element newNode, Object o, Object d) {		
		// note: "" is the default default-value. All text-less nodes would match, even if very non-default... 
		// had to make exception for EAdStrings, which when empty should not be saved
		return ( ! d.equals("") && newNode.getTextContent().equals(d)) 
			|| (o instanceof EAdString && newNode.getTextContent().equals(d))
			|| (o instanceof EAdAssetBundle && newNode.getTextContent().equals(d));
	}

	/**
	 * Determines if an object is empty (whether it's null or whether is empty,
	 * in case of being a list or a map)
	 *
	 * @param o
	 *            the object to check
	 * @return
	 */
	public boolean isEmpty(Object o) {
		return (o == null) 
			|| (o instanceof EAdList && ((EAdList<?>) o).size() == 0) 
			|| (o instanceof EAdMap && ((EAdMap<?, ?>) o).isEmpty()) 
			|| (o instanceof EAdResources && ((EAdResources) o).isEmpty());
	}
}
