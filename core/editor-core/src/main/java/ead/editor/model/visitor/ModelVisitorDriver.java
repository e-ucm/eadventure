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

package ead.editor.model.visitor;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.Param;
import ead.common.model.assets.AssetDescriptor;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.params.EAdParam;
import ead.common.model.params.text.EAdString;
import ead.editor.EditorStringHandler;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EditorNode;
import ead.editor.model.nodes.EngineNode;

/**
 * Visits parts of the model. Given a ModelVisitor and a start, the visitor
 * is driven throught the model in a recursive depth-first fashion. Visitors
 * are expected to say when to stop, and to take notes of anything they like
 * during the way. They will be called once per visited node, and once per
 * discovered searchable property.
 *
 * @author mfreire
 */
public class ModelVisitorDriver {

	private static final Logger logger = LoggerFactory
			.getLogger("ModelVisitorDriver");

	// drivers are reusable; they keep no internal information
	private ElementDriver elementDriver = new ElementDriver();
	private ListDriver listDriver = new ListDriver();
	private MapDriver mapDriver = new MapDriver();
	private ParamDriver paramDriver = new ParamDriver();
	private AssetDriver assetDriver = new AssetDriver();
	private EditorNodeDriver editorNodeDriver = new EditorNodeDriver();
	private EngineNodeDriver engineNodeDriver = new EngineNodeDriver();

	private EditorStringHandler esh;

	private ModelVisitor v = null;

	/**
	 * Visits all elements in data in a depth-first manner. Does not keep track
	 * of duplicate EAdElements (the visitor should do that). Notifies the
	 * visitor of all elements, their properties, and their assets.
	 *
	 * @param data model to visit
	 * @param v visitor
	 */
	public void visit(EAdAdventureModel data, ModelVisitor v,
			EditorStringHandler esh) {
		this.v = v;
		this.esh = esh;
		try {
			// visit the root element, and continue from there
			driveInto(data, null, null);
		} catch (Exception e) {
			logger.error("Error visiting model", e);
		}
	}

	/**
	 * Re-visit a part of the model. The visitor should not return 'true'
	 * visitObject queries which should not be pursued.
	 * @param o
	 * @param v
	 * @param esh
	 */
	public void visit(DependencyNode node, ModelVisitor v,
			EditorStringHandler esh) {
		this.v = v;
		this.esh = esh;
		try {
			// visit the initial node;
			// - if EditorNode, then visit only properties
			// - if general
			driveInto(node, null, null);
		} catch (Exception e) {
			logger.error("Error visiting model", e);
		}
	}

	/**
	 * Returns the correct driver to use for a given object.
	 * @param o object to drive into.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private VisitorDriver driverFor(Object o) {
		if (o instanceof EditorNode) {
			return editorNodeDriver;
		} else if (o instanceof EngineNode) {
			return engineNodeDriver;
		} else if (o instanceof EAdElement) {
			return elementDriver;
		} else if (o instanceof EAdList) {
			return listDriver;
		} else if (o instanceof EAdMap) {
			return mapDriver;
		} else if (o instanceof EAdParam) {
			return paramDriver;
		} else if (o instanceof AssetDescriptor) {
			return assetDriver;
		} else {
			return paramDriver;
		}
	}

	/**
	 * Deepens visit into object o; called by drivers.
	 */
	@SuppressWarnings("unchecked")
	public void driveInto(Object o, Object source, String sourceName) {
		logger.debug("Driving into target of type {} [{}]", new String[] {
				o.getClass().getName(),
				(o instanceof EAdElement ? ((EAdElement) o).getId() : o
						.getClass().getSimpleName()
						+ "@" + o.hashCode()) });

		if (v == null) {
			throw new IllegalStateException("No visitor defined. End of visit.");
		}

		VisitorDriver d = driverFor(o);
		if (d instanceof ParamDriver || d instanceof MapDriver
				|| d instanceof ListDriver) {
			logger.debug("auto-driving into {} of type {} with a {}",
					new Object[] { o.toString(), o.getClass().getSimpleName(),
							d.getClass().getSimpleName() });
			d.drive(o, source, sourceName);
		} else if (v.visitObject(o, source, sourceName)) {
			logger.debug("driving into {} of type {} with a {}", new Object[] {
					o.toString(), o.getClass().getSimpleName(),
					d.getClass().getSimpleName() });
			d.drive(o, source, sourceName);
		}
	}

	/**
	 * visits a DependencyNode.
	 */
	private class EngineNodeDriver implements VisitorDriver<EngineNode> {

		@Override
		public void drive(EngineNode target, Object source, String sourceName) {
			Object o = target.getContent();
			driverFor(o).drive(o, target, "content");
		}
	}

	/**
	 * visits an EditorNode.
	 */
	private class EditorNodeDriver implements VisitorDriver<EditorNode> {

		@Override
		public void drive(EditorNode target, Object source, String sourceName) {

			logger.info("Driving into an EditorNode! Wow!");
			for (String f : target.getIndexedFields()) {
				Object o = readProperty(target, f);
				if (!isEmpty(o)) {
					logger.info("\t'{}' has a '{}' property!", new Object[] {
							target, f });
					if (o instanceof List) {
						driveInto(o, target, f);
					} else {
						v.visitProperty(target, f, o.toString());
					}
				}
			}
		}
	}

	/**
	 * visits an EAdElement.
	 */
	private class ElementDriver implements VisitorDriver<EAdElement> {

		@Override
		public void drive(EAdElement target, Object source, String sourceName) {
			processParams(target);
		}
	}

	public static final String listSuffix = "-list";

	/**
	 * visits a list - either by adding it all as attributes, or some other
	 * method.
	 */
	private class ListDriver implements VisitorDriver<EAdList<?>> {

		@Override
		public void drive(EAdList<?> target, Object source, String sourceName) {
			for (int i = 0; i < target.size(); i++) {
				// visit all children-values of this list
				Object o = target.get(i);
				if (o != null) {
					driveInto(o, source, sourceName + listSuffix);
				}
			}
		}
	}

	public static final String mapKeySuffix = "-map-key";
	public static final String mapValueSuffix = "-map-value";

	/**
	 * visits a map (keys and values).
	 */
	private class MapDriver implements VisitorDriver<EAdMap<?, ?>> {

		@Override
		public void drive(EAdMap<?, ?> target, Object source, String sourceName) {
			int i = 0;
			for (Map.Entry<?, ?> e : target.entrySet()) {
				if (e.getKey() != null && e.getValue() != null) {
					driveInto(e.getKey(), source, sourceName + mapKeySuffix);
					driveInto(e.getValue(), source, sourceName + mapValueSuffix);
				}
				i++;
			}
		}
	}

	/**
	 * visits a param - or any kind of field storable as an XML attribute.
	 * Will only be called if this is not an element, or a list/map
	 */
	private class ParamDriver implements VisitorDriver<Object> {

		@Override
		public void drive(Object target, Object source, String sourceName) {
			if (target == null) {
				logger.warn("Null data");
			} else {
				if (target instanceof EAdString) {
					String value = esh.getString((EAdString) target);
					v.visitProperty(source, sourceName, value);
				} else if (target instanceof Class) {
					String value = ((Class<?>) target).getName();
					v.visitProperty(source, sourceName, value);
				} else if (target instanceof EAdParam) {
					String value = ((EAdParam) target).toStringData();
					v.visitProperty(source, sourceName, value);
				} else {
					v.visitProperty(source, sourceName, target.toString());
				}
			}
		}
	}

	public static final String resourceAssetSuffix = "-inner-asset";
	public static final String resourceBundleSuffix = "-inner-bundle-id";

	/**
	 * visits an asset.
	 */
	private class AssetDriver implements VisitorDriver<AssetDescriptor> {

		@Override
		public void drive(AssetDescriptor target, Object source,
				String sourceName) {
			processParams(target);
		}
	}

	/**
	 * Process field parameters; called by drivers
	 *
	 * @param data object where parameters are to be processed
	 * @param v
	 */
	private void processParams(Object data) {

		logger.debug("Iterating properties of {}", data);

		Class<?> clazz = data.getClass();

		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				try {
					Param param = field.getAnnotation(Param.class);
					String fieldName = field.getName();
					if (param != null) {
						Object o = readProperty(data, fieldName);
						if (!isEmpty(o)) {
							logger.debug("\t'{}' has a '{}' property!",
									new Object[] { data, fieldName });
							driveInto(o, data, fieldName);
						}
					}
				} catch (Exception e) {
					logger.error("Could not access properties of {}, from {}",
							new Object[] { clazz, data.getClass(), e });
				}
			}
			clazz = clazz.getSuperclass();
		}

		logger.debug("Finished properties of {}", data);
	}

	/**
	 * Utility method to find a property descriptor for a single property
	 *
	 * @param c
	 * @param fieldName
	 * @return
	 */
	public static Object readProperty(Object object, String fieldName) {
		Class<?> c = object.getClass();
		try {
			PropertyDescriptor pd = null;
			for (PropertyDescriptor d : Introspector.getBeanInfo(c)
					.getPropertyDescriptors()) {
				if (d.getName().equals(fieldName)) {
					pd = d;
				}
			}
			if (pd == null) {
				logger.error("Missing descriptor for {} -- trace follows", c
						+ "." + fieldName, new Exception());
				return null;
			}
			Method method = pd.getReadMethod();
			if (method == null) {
				logger.error("Missing read-method for {} in {} ", fieldName, c);
				logger.error("Read-method fail from ", new Exception());
				return null;
			}
			logger.debug("\t invoking {}", fieldName);
			return method.invoke(object);
		} catch (Exception e) {
			logger.error("Error calling getter for " + "field {} in class {} ",
					new Object[] { fieldName, c }, e);
			return null;
		}
	}

	/**
	 * Determines if an object is null or empty. This includes empty maps or
	 * lists
	 *
	 * @param o the object to check
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		return ((o == null)
				|| (o instanceof Collection && ((Collection<?>) o).isEmpty())
				|| (o instanceof EAdList && ((EAdList<?>) o).size() == 0) || (o instanceof EAdMap && ((EAdMap<?, ?>) o)
				.isEmpty()));
	}

	/**
	 * Implemented by actual drivers
	 *
	 * @param <T>
	 */
	private interface VisitorDriver<T> {

		/**
		 * Drive to another node
		 *
		 * @param target the target node
		 * @param source the source node; only EAdElements can serve as sources
		 * @param sourceName the name of the property within the source that we
		 * are acting on
		 */
		public abstract void drive(T target, Object source, String sourceName);
	}
}
