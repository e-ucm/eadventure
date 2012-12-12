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

import java.beans.IntrospectionException;
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
import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.params.EAdParam;
import ead.common.params.text.EAdString;
import ead.common.resources.BasicAssetBundle;
import ead.common.resources.BasicResources;
import ead.common.resources.EAdAssetBundle;
import ead.common.resources.EAdBundleId;
import ead.common.resources.EAdResources;
import ead.common.resources.assets.AssetDescriptor;
import ead.editor.EditorStringHandler;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EditorNode;

public class ModelVisitorDriver {

	private static final Logger logger = LoggerFactory
			.getLogger("ModelVisitorDriver");

	// drivers are reusable; they keep no internal information
	private ElementDriver elementDriver = new ElementDriver();
	private ListDriver listDriver = new ListDriver();
	private MapDriver mapDriver = new MapDriver();
	private ParamDriver paramDriver = new ParamDriver();
	private AssetDriver assetDriver = new AssetDriver();
	private ResourceDriver resourceDriver = new ResourceDriver();
	private EditorNodeDriver editorNodeDriver = new EditorNodeDriver();
	private DependencyNodeDriver dependencyNodeDriver = new DependencyNodeDriver();

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
	 * Re-index a part of the model.
	 * @param o
	 * @param v
	 * @param esh 
	 */
	public void visit(DependencyNode o, ModelVisitor v,
			EditorStringHandler esh) {
		this.v = v;
		this.esh = esh;
		try {
			// visit the root element, and continue from there
			driveInto(o, null, null);
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
		} else if (o instanceof EAdElement) {
			return elementDriver;
		} else if (o instanceof EAdList) {
			return listDriver;
		} else if (o instanceof EAdMap) {
			return mapDriver;
		} else if (o instanceof EAdParam) {
			return paramDriver;
		} else if (o instanceof EAdResources) {
			return resourceDriver;
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

		if (driverFor(o) instanceof ParamDriver) {
			((ParamDriver) driverFor(o)).drive(o, source, sourceName);
		} else if (v.visitObject(o, source, sourceName)) {
			logger.debug("driving into {} of type {} with a {}", new Object[] {
					o.toString(), o.getClass().getSimpleName(),
					driverFor(o).getClass().getSimpleName() });
			driverFor(o).drive(o, source, sourceName);
		}
	}

	/**
	 * visits a DependencyNode.
	 */
	private class DependencyNodeDriver implements VisitorDriver<DependencyNode> {

		@Override
		public void drive(DependencyNode target, Object source, String sourceName) {
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

			Class<?> c = target.getClass();
			for (String f : target.getIndexedFields()) {
				try {
					PropertyDescriptor pd = getPropertyDescriptor(c, f);
					if (pd == null) {
						logger.error("Missing descriptor for {} in {} ",
								f, c);
						continue;
					}
					Method method = pd.getReadMethod();
					if (method == null) {
						logger.error("Missing read-method for {} in {} ",
								f, c);
						continue;
					}
					logger.debug("\t invoking {}", method.getName());
					Object o = method.invoke(target);
					if (!isEmpty(o)) {
						logger.debug("\t'{}' has a '{}' property!",
								new Object[] { target, pd.getName() });
						if (o instanceof List) {
							// explicitly allow lists
							List<?> list = (List<?>)o;
							for (Object lo : list) {
								v.visitProperty(target, f, lo.toString());
							}
						} else if (o instanceof Collection) {
							// we do not like any other type of collections
							throw new IllegalArgumentException(
									"Only lists and stringifiables are "
									+ "acceptable EditorNode indexed-fields");							
					    } else {
							v.visitProperty(target, f, o.toString());
						}
					}
				} catch (Exception e) {
					logger.error("Error accessing field {} in {}", new Object[]{f, c}, e);
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

	/**
	 * visits a list - either by adding it all as attributes, or some other
	 * method.
	 */
	private class ListDriver implements VisitorDriver<List<?>> {

		@Override
		public void drive(List<?> target, Object source, String sourceName) {
			for (int i = 0; i < target.size(); i++) {
				// visit all children-values of this list
				Object o = target.get(i);
				if (o != null) {
					driveInto(o, source, sourceName + "-list");
				}
			}
		}
	}

	/**
	 * visits a map (keys and values).
	 */
	private class MapDriver implements VisitorDriver<EAdMap<?, ?>> {

		@Override
		public void drive(EAdMap<?, ?> target, Object source, String sourceName) {
			int i = 0;
			for (Map.Entry<?, ?> e : target.entrySet()) {
				if (e.getKey() != null && e.getValue() != null) {
					driveInto(e.getKey(), source, sourceName + "-map-key");
					driveInto(e.getValue(), source, sourceName + "-map-value");
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

	/**
	 * visits a resource.
	 */
	private class ResourceDriver implements VisitorDriver<EAdResources> {

		@Override
		public void drive(EAdResources target, Object source, String sourceName) {
			if (target.getInitialBundle() != null) {
				v.visitProperty(target, "initial-bundle-id", target
						.getInitialBundle().getBundleId());
			}

			int i = 0;
			for (String assetId : ((BasicAssetBundle) target).getIds()) {
				AssetDescriptor asset = ((BasicAssetBundle) target)
						.getAsset(assetId);
				driveInto(asset, target, "inner-asset-" + i);
				i++;
			}

			for (EAdBundleId bundleId : ((BasicResources) target)
					.getBundleIds()) {
				EAdAssetBundle bundle = ((BasicResources) target)
						.getBundle(bundleId);

				int j = 0;
				for (String assetId : ((BasicAssetBundle) bundle).getIds()) {
					AssetDescriptor asset = ((BasicAssetBundle) bundle)
							.getAsset(assetId);
					driveInto(asset, target, "inner-asset-" + j);
					j++;
				}
			}
		}
	}

	/**
	 * visits an asset.
	 */
	private class AssetDriver implements VisitorDriver<AssetDescriptor> {

		@Override
		public void drive(AssetDescriptor target, Object source,
				String sourceName) {
			v.visitProperty(target, "asset-class", target.getClass().getName());
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
					if (param != null) {
						PropertyDescriptor pd = getPropertyDescriptor(data
								.getClass(), field.getName());
						if (pd == null) {
							logger.error("Missing descriptor for {} in {} ",
									field.getName(), data.getClass());
							continue;
						}
						Method method = pd.getReadMethod();
						if (method == null) {
							logger.error("Missing read-method for {} in {} ",
									field.getName(), data.getClass());
							continue;
						}
						logger.debug("\t invoking {}", field.getName());
						Object o = method.invoke(data);
						if (!isEmpty(o)) {
							logger.debug("\t'{}' has a '{}' property!",
									new Object[] { data, pd.getName() });
							driveInto(o, data, pd.getName());
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
	public static PropertyDescriptor getPropertyDescriptor(Class<?> c,
			String fieldName) {
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(c)
					.getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			logger.error("Could not find getters or setters for "
					+ "field {} in class {} ", new Object[] { fieldName,
					c.getCanonicalName(), e });
		}
		return null;
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
				|| (o instanceof EAdList && ((EAdList<?>) o).size() == 0)
				|| (o instanceof EAdMap && ((EAdMap<?, ?>) o).isEmpty()) || (o instanceof EAdResources && ((EAdResources) o)
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
