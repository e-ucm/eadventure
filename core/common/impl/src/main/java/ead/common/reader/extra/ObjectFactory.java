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

package ead.common.reader.extra;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ead.common.model.EAdElement;
import ead.common.params.EAdParam;
import ead.common.reader.ProxyElement;
import ead.common.reader.visitors.NodeVisitor;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.EAdMatrix;
import ead.common.util.BasicMatrix;

/**
 * Includes methods to generate an object of a given type from a string value
 */
public class ObjectFactory {

	private static final Logger logger = Logger.getLogger("ObjectFactory");
	
	private static Map<String, Object> paramsMap = new HashMap<String, Object>();
	private static Map<String, AssetDescriptor> assetsMap = new HashMap<String, AssetDescriptor>();
	private static Map<String, EAdElement> elementsMap = new HashMap<String, EAdElement>();
	private static List<ProxyElement> proxies = new ArrayList<ProxyElement>();

	@SuppressWarnings("unchecked")
	public static Object getObject(String value, Class<?> fieldType) {
		if ( EAdParam.class.isAssignableFrom(fieldType)){
			EAdParam param = constructParam( value, (Class<? extends EAdParam>) fieldType );
			return param;
		} else if ( EAdElement.class.isAssignableFrom(fieldType)){
			EAdElement element = elementsMap.get(value);
			if (value != null && !value.equals("") && element == null) {
				ProxyElement proxy = new ProxyElement(value);
				proxies.add(proxy);
				return proxy;
			}
			return element;
		} else if ( AssetDescriptor.class.isAssignableFrom(fieldType)){
			return assetsMap.get(value);
		}
		else if (fieldType == String.class )
			return value;
		else if (fieldType == Integer.class || fieldType == int.class)
			return Integer.parseInt(value);
		else if (fieldType == Boolean.class || fieldType == boolean.class)
			return Boolean.parseBoolean(value);
		else if (fieldType == Float.class || fieldType == float.class)
			return Float.parseFloat(value);
		else if (fieldType == Character.class || fieldType == char.class){
			return new Character(value.charAt(0));
		}
		else if (fieldType == EAdBundleId.class)
			return new EAdBundleId(value);
		else if (fieldType == EAdMatrix.class || fieldType == BasicMatrix.class)
			return BasicMatrix.parse(value);
		else if (fieldType == Class.class)
			try {
				return ClassLoader.getSystemClassLoader().loadClass(value);
			} catch (ClassNotFoundException e) {
				return Object.class;
			}
		else if (fieldType.isEnum())
			return Enum.valueOf(fieldType.asSubclass(Enum.class), value);
		else {
			if (elementsMap.containsKey(value)) {
				//FIXME This could be a temporary fix
				EAdElement element = elementsMap.get(value);
				logger.info("The field type was not recognised. The EAdElement is returned");
				return element;
			} else {
				logger.info("The field type \"" + fieldType + "\"  was not recognised. The string is returned");
				return value;
			}
		}

	}

	public static void initilize() {
		paramsMap.clear();
		elementsMap.clear();
		assetsMap.clear();
		proxies.clear();
	}

	public static void addElement( String id, EAdElement element){
		elementsMap.put(id, element);
		int i = 0;
		while (i < proxies.size()) {
			if (proxies.get(i).getValue().equals(id)) {
				if (proxies.get(i).getField() != null)
					NodeVisitor.setValue(proxies.get(i).getField(), proxies.get(i).getParent(), element);
				else if (proxies.get(i).getList() != null) {
					proxies.get(i).getList().remove(proxies.get(i).getListPos());
					proxies.get(i).getList().add(element, proxies.get(i).getListPos());
				}
				proxies.remove(i);
			} else
				i++;
		}
	}
	
	public static Map<String, Object> getParamsMap() {
		return paramsMap;
	}
	public static void addAsset( String id, AssetDescriptor descriptor ){
		assetsMap.put(id, descriptor);
	}
	
	private static EAdParam constructParam( String value, Class<? extends EAdParam> clazz ){
		try {
			Constructor<?> c = clazz.getConstructor(String.class);
			return (EAdParam) c.newInstance(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
