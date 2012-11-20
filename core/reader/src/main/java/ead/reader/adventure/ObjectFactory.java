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

package ead.reader.adventure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.EAdElement;
import ead.common.params.EAdParam;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.BasicMatrix;
import ead.common.util.EAdMatrix;
import ead.common.util.EAdPosition;
import ead.common.util.EAdRectangle;
import ead.common.util.EAdURI;
import ead.reader.adventure.visitors.NodeVisitor;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionProvider;

/**
 * Includes methods to generate an object of a given type from a string value
 */
public class ObjectFactory {

	private static final Logger logger = LoggerFactory
			.getLogger("ObjectFactory");

	private static Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
	private static Map<String, AssetDescriptor> assetsMap = new LinkedHashMap<String, AssetDescriptor>();
	private static Map<String, EAdElement> elementsMap = new LinkedHashMap<String, EAdElement>();
	private static Map<String, ArrayList<ProxyElement>> proxies = new LinkedHashMap<String, ArrayList<ProxyElement>>();

	private static ReflectionProvider reflectionProvider;

	public static void init(ReflectionProvider reflectionProv) {
		reflectionProvider = reflectionProv;
	}

	@SuppressWarnings("unchecked")
	public static Object getObject(String value, Class<?> fieldType) {
		if (reflectionProvider == null) {
			throw new IllegalStateException(
					"ObjectFactory.init() must be called before using it.");
		}

		if (fieldType == String.class) {
			return value;
		} else if (fieldType == Integer.class || fieldType == int.class) {
			return Integer.parseInt(value);
		} else if (fieldType == Boolean.class || fieldType == boolean.class) {
			return Boolean.parseBoolean(value);
		} else if (fieldType == Float.class || fieldType == float.class) {
			return Float.parseFloat(value);
		} else if (fieldType == Character.class || fieldType == char.class) {
			return new Character(value.charAt(0));
		} else if (fieldType == EAdBundleId.class) {
			return new EAdBundleId(value);
		} else if (fieldType == EAdMatrix.class
				|| fieldType == BasicMatrix.class) {
			return BasicMatrix.parse(value);
		} else if (fieldType == Class.class) {
			return getClassFromName(value);
		} else if (fieldType.isEnum()) {
			@SuppressWarnings("rawtypes")
			Class<? extends Enum> enumClass = (Class<? extends Enum>) fieldType;
			return Enum.valueOf(enumClass, value);
		} else if (reflectionProvider.isAssignableFrom(EAdParam.class,
				fieldType)) {
			EAdParam param = constructParam(value,
					(Class<? extends EAdParam>) fieldType);
			return param;
		} else if (reflectionProvider.isAssignableFrom(EAdElement.class,
				fieldType)) {
			EAdElement element = elementsMap.get(value);
			if (value != null && !value.equals("") && element == null) {
				logger.debug("Substituting proxy for {}", value);
				ArrayList<ProxyElement> pending = proxies.get(value);
				if (pending == null) {
					pending = new ArrayList<ProxyElement>();
					proxies.put(value, pending);
				}
				ProxyElement proxy = new ProxyElement(value);
				pending.add(proxy);
				return proxy;
			} else {
				return element;
			}
		} else if (reflectionProvider.isAssignableFrom(AssetDescriptor.class,
				fieldType)) {
			return assetsMap.get(value);
		} else {
			logger.info("The field type " + fieldType + "with value " + value
					+ " was not recognised. The string is returned");
			return value;
		}
	}

	public static void initialize() {
		paramsMap.clear();
		elementsMap.clear();
		assetsMap.clear();
		proxies.clear();
	}

	public static boolean checkProxies() {
		return proxies.isEmpty();
	}

	/**
	 * Adds an element to the proxies map. Also purges any existing proxies.
	 * 
	 * @param id
	 * @param element
	 */
	public static void addElement(String id, EAdElement element) {
		logger.debug("Element with id {} added to element map", id);
		elementsMap.put(id, element);
		ArrayList<ProxyElement> pending = proxies.get(id);
		if (pending != null) {
			proxies.remove(id);
			logger.debug("Finally filling in {} proxies for {}",
					pending.size(), id);
			for (ProxyElement p : pending) {
				if (p.getField() != null) {
					logger
							.debug("Setting value for {} at {} in {}",
									new String[] {
											id,
											p.getField().getName(),
											NodeVisitor.objectToString(p
													.getParent()) });
					NodeVisitor.setValue(p.getField(), p.getParent(), element);
				} else if (p.getList() != null) {
					logger.debug("Setting value for {}, element {} of list {}",
							new String[] { id, "" + p.getListPos(),
									"" + p.getList().hashCode() });
					p.getList().remove(p.getListPos());
					p.getList().add(element, p.getListPos());
				} else {
					logger.warn("Proxy for {} is neither list nor field!", id);
				}
			}
		}
	}

	public static void addAsset(String id, AssetDescriptor descriptor) {
		assetsMap.put(id, descriptor);
	}

	public static Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	private static EAdParam constructParam(String value,
			Class<? extends EAdParam> clazz) {
		if (clazz.equals(EAdString.class)) {
			return new EAdString(value);
		} else if (clazz.equals(ColorFill.class)) {
			return new ColorFill(value);
		} else if (clazz.equals(LinearGradientFill.class)) {
			return new LinearGradientFill(value);
		} else if (clazz.equals(Paint.class)) {
			return new Paint(value);
		} else if (clazz.equals(EAdPosition.class)) {
			return new EAdPosition(value);
		} else if (clazz.equals(EAdRectangle.class)) {
			return new EAdRectangle(value);
		} else if (clazz.equals(EAdURI.class)) {
			return new EAdURI(value);
		}

		logger.error("Param class {} needs "
				+ "an explicitly defined constructor", clazz);
		return null;
	}

	public static Class<?> getClassFromName(String clazz) {
		if (clazz.equals("java.lang.Float")) {
			return Float.class;
		} else if (clazz.equals("java.lang.Integer")) {
			return Integer.class;
		} else if (clazz.equals("java.lang.Boolean")) {
			return Boolean.class;
		} else if (clazz.equals("java.lang.Class")) {
			return Class.class;
		} else if (clazz.equals("java.lang.Character")) {
			return Character.class;
		} else if (clazz.equals("java.lang.String")) {
			return String.class;
		} else {
			try {
				Class<?> clazz2 = ReflectionClassLoader.getReflectionClass(
						clazz).getType();
				return clazz2;
			} catch (Exception e) {
				logger.warn(
						"Not match for class {}. Object.class was returned",
						clazz);
				return Object.class;
			}
		}
	}
}
