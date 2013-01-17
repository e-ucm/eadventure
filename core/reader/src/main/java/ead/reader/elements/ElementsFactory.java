package ead.reader.elements;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.features.Identified;
import ead.common.params.EAdParam;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.util.EAdPosition;
import ead.common.util.EAdRectangle;
import ead.common.util.EAdURI;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;

public class ElementsFactory {

	private static final Logger logger = LoggerFactory
			.getLogger("ElementsFactory");

	private Map<String, EAdParam> paramsMap;

	private Map<String, Identified> elementsMap;

	private Map<String, Identified> assetsMap;

	public ElementsFactory() {
		paramsMap = new HashMap<String, EAdParam>();
		elementsMap = new HashMap<String, Identified>();
		assetsMap = new HashMap<String, Identified>();
	}

	public Object getParam(String textValue, Class<?> clazz) {
		// Check for a EAdParam
		Object result = constructEAdParam(textValue, clazz);
		// Check for a simple type
		if (result == null) {
			result = constructSimpleParam(textValue, clazz);
		}

		if (result == null) {
			logger.warn("No constructor for parameter class {}", clazz);
		}
		return result;
	}

	/**
	 * Constructs and EAdParam from its literal representation
	 * 
	 * @param value
	 *            text value representing the param
	 * @param clazz
	 *            the parameter class
	 * @return
	 */
	private EAdParam constructEAdParam(String value, Class<?> clazz) {
		EAdParam p = paramsMap.get(value);
		if (p != null) {
			return p;
		}

		if (clazz.equals(EAdString.class)) {
			p = new EAdString(value);
		} else if (clazz.equals(ColorFill.class)) {
			p = new ColorFill(value);
		} else if (clazz.equals(LinearGradientFill.class)) {
			p = new LinearGradientFill(value);
		} else if (clazz.equals(Paint.class)) {
			p = new Paint(value);
		} else if (clazz.equals(EAdPosition.class)) {
			p = new EAdPosition(value);
		} else if (clazz.equals(EAdRectangle.class)) {
			p = new EAdRectangle(value);
		} else if (clazz.equals(EAdURI.class)) {
			p = new EAdURI(value);
		}

		if (p != null) {
			paramsMap.put(value, p);
		}
		return p;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	private Object constructSimpleParam(String value, Class<?> clazz) {
		if (clazz == String.class) {
			return value;
		} else if (clazz == Integer.class || clazz == int.class) {
			return Integer.parseInt(value);
		} else if (clazz == Boolean.class || clazz == boolean.class) {
			return Boolean.parseBoolean(value);
		} else if (clazz == Float.class || clazz == float.class) {
			return Float.parseFloat(value);
		} else if (clazz == Character.class || clazz == char.class) {
			return new Character(value.charAt(0));
		} else if (clazz == Class.class) {
			return getClassFromName(value);
		} else if (clazz.isEnum()) {
			Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz;
			return Enum.valueOf(enumClass, value);
		}
		return null;
	}

	/**
	 * Returns the class object for the given class string
	 * 
	 * @param clazz
	 * @return
	 */
	public Class<?> getClassFromName(String clazz) {
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

	/**
	 * Create an object of the given class
	 * 
	 * @param clazz
	 * @return
	 */
	public Object createObject(Class<?> clazz) {
		ReflectionClass<?> classType = ReflectionClassLoader
				.getReflectionClass(clazz);
		if (classType.getConstructor() != null) {
			return classType.getConstructor().newInstance();
		}
		return null;
	}

	public void putAsset(String uniqueId, Identified assetDescriptor) {
		assetsMap.put(uniqueId, assetDescriptor);
	}

	public void putEAdElement(String uniqueId, Identified eadElement) {
		elementsMap.put(uniqueId, eadElement);
	}

	public Identified getAsset(String uniqueId) {
		return assetsMap.get(uniqueId);
	}

	public Identified getEAdElement(String uniqueId) {
		return elementsMap.get(uniqueId);
	}

}
