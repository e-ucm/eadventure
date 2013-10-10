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

package es.eucm.ead.reader.model;

import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.trajectories.NodeTrajectory;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.DragGEv;
import es.eucm.ead.model.params.guievents.KeyGEv;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Matrix;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Rectangle;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.model.params.variables.VarDef;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectsFactory {

	static private Logger logger = LoggerFactory
			.getLogger(ObjectsFactory.class);

	private Map<Class<?>, Map<String, Object>> paramsMap;

	private Map<String, Identified> elementsMap;

	private Map<String, AssetDescriptor> assetsMap;

	private ReflectionProvider reflectionProvider;

	private Map<String, EAdVarDef<?>> registeredVars;

	private XMLVisitor xmlVisitor;

	public ObjectsFactory(ReflectionProvider reflectionProvider,
			XMLVisitor xmlVisitor) {
		this.xmlVisitor = xmlVisitor;
		this.reflectionProvider = reflectionProvider;
		paramsMap = new HashMap<Class<?>, Map<String, Object>>();
		elementsMap = new HashMap<String, Identified>();
		assetsMap = new HashMap<String, AssetDescriptor>();
		registeredVars = new HashMap<String, EAdVarDef<?>>();
		addDefaultVars();
	}

	private void addDefaultVars() {
		registeredVars.put("alpha", SceneElement.VAR_ALPHA);
		registeredVars.put("visible", SceneElement.VAR_VISIBLE);
		registeredVars.put("x", SceneElement.VAR_X);
		registeredVars.put("y", SceneElement.VAR_Y);
		registeredVars.put("rotation", SceneElement.VAR_ROTATION);
		registeredVars.put("scale", SceneElement.VAR_SCALE);
		registeredVars.put("scale_x", SceneElement.VAR_SCALE_X);
		registeredVars.put("scale_y", SceneElement.VAR_SCALE_Y);
		registeredVars.put("enable", SceneElement.VAR_ENABLE);
		registeredVars.put("disp_x", SceneElement.VAR_DISP_X);
		registeredVars.put("disp_y", SceneElement.VAR_DISP_Y);
		registeredVars.put("z", SceneElement.VAR_Z);
		registeredVars.put("state", SceneElement.VAR_STATE);
		registeredVars.put("orientation", SceneElement.VAR_ORIENTATION);
		registeredVars.put("influence_area", NodeTrajectory.VAR_INFLUENCE_AREA);
		registeredVars.put("bundle", SceneElement.VAR_BUNDLE_ID);
		registeredVars.put("sound_on", SystemFields.SOUND_ON.getVarDef());

	}

	public Object getParam(String textValue, Class<?> clazz) {
		Object result = null;

		Map<String, Object> map = paramsMap.get(clazz);
		if (map == null) {
			map = new HashMap<String, Object>();
			paramsMap.put(clazz, map);
		} else {
			result = map.get(textValue);
		}

		if (result == null) {
			// Check for a EAdParam
			result = constructEAdParam(textValue, clazz);
		}

		// Check for a simple type
		if (result == null) {
			result = constructSimpleParam(textValue, clazz);
		}

		if (result == null) {
			logger.warn("No constructor for parameter class {}", clazz);
		} else {
			map.put(textValue, result);
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Object getObject(String value, Class<?> c) {
		Object result = null;
		if (reflectionProvider.isAssignableFrom(EAdElement.class, c)) {
			result = elementsMap.get(value);

		} else if (reflectionProvider
				.isAssignableFrom(AssetDescriptor.class, c)) {
			result = assetsMap.get(value);
		} else if (reflectionProvider.isAssignableFrom(EAdList.class, c)) {
			EAdList list = new EAdList();
			// Remove [ and final ]
			// XXX
			String[] elements = value.substring(1, value.length() - 2).split(
					",");
			for (String e : elements) {
				if (e.length() > 0) {

				} else {
				}
			}
			result = list;
		} else if (reflectionProvider.isAssignableFrom(EAdMap.class, c)) {
			result = new EAdMap();
			// XXX
			logger.warn("OMG, a map! This needs implementation");
		} else {
			result = getParam(value, c);
			if (result == null) {
				logger.warn("Not possible to parse initial value: {}", value);
			}
		}
		return result;
	}

	/**
	 * Constructs and EAdParam from its literal representation
	 *
	 * @param value text value representing the param
	 * @param clazz the parameter class
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	private EAdParam constructEAdParam(String value, Class<?> clazz) {
		EAdParam p = null;
		if (clazz.equals(EAdString.class)) {
			p = new EAdString(value);
		} else if (clazz.equals(ColorFill.class)) {
			p = new ColorFill(value);
		} else if (clazz.equals(LinearGradientFill.class)) {
			p = new LinearGradientFill(value);
		} else if (clazz.equals(Paint.class)) {
			p = new Paint(value);
		} else if (clazz.equals(Position.class)) {
			p = new Position(value);
		} else if (clazz.equals(Rectangle.class)) {
			p = new Rectangle(value);
		} else if (clazz.equals(MouseGEv.class)) {
			p = new MouseGEv(value);
		} else if (clazz.equals(KeyGEv.class)) {
			p = new KeyGEv(value);
		} else if (clazz.equals(DragGEv.class)) {
			p = new DragGEv(value);
		} else if (clazz.equals(VarDef.class)) {
			p = registeredVars.get(value);
			if (p == null) {
				try {
					Object initialValue = null;
					String values[] = value.split(";");
					Class<?> c = getClassFromName(values[1]);

					boolean forLater = false;
					if (values.length == 3) {
						initialValue = this.getObject(values[2], c);
						forLater = initialValue == null
								&& !"null".equals(values[2]);
					}
					p = new VarDef(values[0], c, initialValue);
					if (forLater) {
						xmlVisitor.addLoadInitalValue((VarDef) p, values[2]);
					}
				} catch (Exception e) {
					logger.warn("VarDef with representation {} poorly parsed",
							value);
				}
			}
		}
		return p;
	}

	private EAdList getList(String value) {
		//EAdList list =
		return null;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	private Object constructSimpleParam(String value, Class<?> clazz) {
		if (clazz == String.class) {
			return value;
		} else if (clazz == Integer.class || clazz == int.class) {
			return Integer.parseInt(value);
		} else if (clazz == Boolean.class || clazz == boolean.class) {
			return value.equals("t") || value.equals("true") ? Boolean.TRUE
					: Boolean.FALSE;
		} else if (clazz == Float.class || clazz == float.class) {
			return Float.parseFloat(value);
		} else if (clazz == Character.class || clazz == char.class) {
			return value.charAt(0);
		} else if (clazz == Class.class) {
			return getClassFromName(value);
		} else if (clazz.isEnum()) {
			Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz;
			return Enum.valueOf(enumClass, value);
		} else if (clazz == Matrix.class) {
			return new Matrix(value);
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
		assetsMap.put(uniqueId, (AssetDescriptor) assetDescriptor);
	}

	public void putEAdElement(String uniqueId, Identified eadElement) {
		elementsMap.put(uniqueId, eadElement);
	}

	public Identified getAsset(String uniqueId) {
		Identified i = assetsMap.get(uniqueId);
		if (i == null) {
			logger.warn("No asset for id {}", uniqueId);
		}
		return i;
	}

	public Identified getEAdElement(String uniqueId) {
		return elementsMap.get(uniqueId);
	}

	public Object getReferencedElement(Object value) {
		Object result = this.getAsset(value.toString());
		if (result == null) {
			result = this.getEAdElement(value.toString());
		}
		return result;
	}

	public void clear() {
		assetsMap.clear();
		elementsMap.clear();
		paramsMap.clear();
	}

	public Collection<AssetDescriptor> getAssets() {
		return assetsMap.values();
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public EAdField<?> getField(String elementId, String varName) {
		EAdElement element = null;
		if (!elementId.equals("ead")) {
			element = (EAdElement) this.getEAdElement(elementId);
			if (element == null) {
				element = new BasicElement(elementId);
			}
		}
		EAdVarDef varDef = registeredVars.get(varName);
		if (varDef == null) {
			logger.warn("No variable registered for {}", varName);
		} else {
			return new BasicField<Boolean>(element, varDef);
		}
		return null;
	}

	public EAdVarDef<?> getVarDef(String key) {
		return registeredVars.get(key);
	}

	public void registerVariable(String string, EAdVarDef<?> var) {
		registeredVars.put(string, var);

	}

	public EAdPaint getPaint(String p) {
		EAdPaint paint = null;
		if (p.indexOf(Paint.SEPARATOR) != -1) {
			paint = (EAdPaint) getParam(p, Paint.class);
		} else if (p.indexOf(LinearGradientFill.SEPARATOR) != -1) {
			paint = (EAdPaint) getParam(p, LinearGradientFill.class);
		} else {
			paint = (EAdPaint) getParam(p, ColorFill.class);
		}
		return paint;
	}

}
