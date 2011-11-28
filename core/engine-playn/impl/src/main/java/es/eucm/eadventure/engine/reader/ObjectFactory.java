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

package es.eucm.eadventure.engine.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gwtent.reflection.client.TypeOracle;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdParam;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.impl.PlayNReflectionProvider;

/**
 * Includes methods to generate an object of a given type from a string value
 */
public class ObjectFactory {

	private static final Logger logger = Logger.getLogger("ObjectFactory");

	private static Map<String, Object> paramsMap = new HashMap<String, Object>();
	private static Map<String, AssetDescriptor> assetsMap = new HashMap<String, AssetDescriptor>();
	private static Map<String, EAdElement> elementsMap = new HashMap<String, EAdElement>();
	private static List<ProxyElement> proxies = new ArrayList<ProxyElement>();

	private static PlayNReflectionProvider reflectionProvider = new PlayNReflectionProvider();

	public static Object getObject(String value, Class<?> fieldType) {
		if (reflectionProvider.isAssignableFrom(EAdParam.class, fieldType)) {
			@SuppressWarnings("unchecked")
			EAdParam param = constructParam(value, (Class<? extends EAdParam>) fieldType);
			return param;
		} else if (reflectionProvider.isAssignableFrom(EAdElement.class,
				fieldType)) {
			EAdElement element = elementsMap.get(value);
			if (value != null && !value.equals("") && element == null) {
				return new ProxyElement(value);
			}
			return element;
		} else if (reflectionProvider.isAssignableFrom(AssetDescriptor.class,
				fieldType)) {
			return assetsMap.get(value);
		} else if (fieldType == String.class)
			return value;
		else if (fieldType == Integer.class || fieldType == int.class)
			return Integer.parseInt(value);
		else if (fieldType == Boolean.class || fieldType == boolean.class)
			return Boolean.parseBoolean(value);
		else if (fieldType == Float.class || fieldType == float.class)
			return Float.parseFloat(value);
		else if (fieldType == Character.class || fieldType == char.class) {
			return new Character(value.charAt(0));
		} else if (fieldType == EAdBundleId.class)
			return new EAdBundleId(value);
		else if (fieldType == Class.class)
			return getClassFromName(value);
		else if (fieldType.isEnum()) {
			Class<? extends Enum> enumClass = (Class<? extends Enum>) fieldType;
			return Enum.valueOf(enumClass, value);
		} else {
			if (elementsMap.containsKey(value)) {
				// FIXME This could be a temporary fix
				EAdElement element = elementsMap.get(value);
				logger.info("The field type was not recognised. The EAdElement is returned");
				return element;
			} else {
				logger.info("The field type " + fieldType + "with value " + value + " was not recognised. The string is returned");
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

	public static void addElement(String id, EAdElement element) {
		elementsMap.put(id, element);
		int i = 0;
		while (i < proxies.size()) {
			if (proxies.get(i).getValue().equals(id)) {
				NodeVisitor.setValue(proxies.get(i).getField(), proxies.get(i).getParent(), element);
				proxies.remove(i);
			} else
				i++;
		}
	}

	public static void addAsset(String id, AssetDescriptor descriptor) {
		assetsMap.put(id, descriptor);
	}

	public static Map<String, Object> getParamsMap() {
		return paramsMap;
	}
	
	private static EAdParam constructParam( String value, Class<? extends EAdParam> clazz ) {
		if (clazz.equals(EAdString.class))
			return new EAdString(value);
		if (clazz.equals(EAdColor.class))
			return new EAdColor(value);
		if (clazz.equals(EAdLinearGradient.class))
			return new EAdLinearGradient(value);
		if (clazz.equals(EAdPaintImpl.class))
			return new EAdPaintImpl(value);
		if (clazz.equals(EAdFontImpl.class))
			return new EAdFontImpl(value);
		if (clazz.equals(EAdPositionImpl.class))
			return new EAdPositionImpl(value);
		if (clazz.equals(EAdRectangleImpl.class)) 
			return new EAdRectangleImpl(value);
		if (clazz.equals(EAdURIImpl.class)) 
			return new EAdURIImpl(value);
		
		logger.severe("Param class " + clazz + " needs constructor explicitly defined");
		return null;
	}

	public static Class<?> getClassFromName(String clazz) {
		if (clazz.equals("java.lang.Float"))
			return  Float.class;
		else if (clazz.equals("java.lang.Integer"))
			return Integer.class;
		else if (clazz.equals("java.lang.Boolean"))
			return Boolean.class;
		else if (clazz.equals("java.lang.Class"))
			return Class.class;
		else if (clazz.equals("java.lang.Character"))
			return Class.class;
		else
			return TypeOracle.Instance.getClassType(clazz).getDeclaringClass();
	}	


}
