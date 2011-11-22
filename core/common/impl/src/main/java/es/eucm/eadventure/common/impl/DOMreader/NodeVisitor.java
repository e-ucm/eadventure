package es.eucm.eadventure.common.impl.DOMreader;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public abstract class NodeVisitor<T> {
	
	protected static final Logger logger = Logger.getLogger("NodeVisitor");

	private static String packageName;
	
	protected static String loaderType;

	public static void init(String packageN) {
		packageName = packageN;
		loaderType = DOMTags.CLASS_AT;
		//loaderType = DOMTags.TYPE_AT;
	}

	public abstract T visit(Node node);
	
	public abstract String getNodeType();
	
	protected String translateClass(String clazz) {
		return clazz != null && clazz.startsWith(".") ? packageName + clazz : clazz;
	}
	
	protected void readFields(Object element, Node node) {
		NodeList nl = node.getChildNodes();
		
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			Node newNode = nl.item(i);
			Field field = getField(element, newNode.getAttributes().getNamedItem(DOMTags.PARAM_AT).getNodeValue());

			if (newNode.getNodeName().equals(DOMTags.PARAM_AT))
				parseParam(element, newNode, field);
			
			if (newNode.getNodeName().equals("resources"))
				parseResources(element, newNode, field);
			
			//TODO use constant
			if (newNode.getNodeName().equals("list"))
				parseList(element, newNode, field);
			
			if (newNode.getNodeName().equals("map"))
				parseMap(element, newNode, field);

		}
	}
	
	private void parseResources(Object element, Node newNode, Field field) {
		boolean accessible = field.isAccessible();
		try {
			field.setAccessible(true);
			EAdResources resources = (EAdResources) field.get(element);
			String initialBundleId = newNode.getAttributes().getNamedItem("initialBundle").getNodeValue();

			NodeList nl = newNode.getChildNodes();
			
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				if (nl.item(i).getNodeName().equals("bundle")) {
					String bundleId = nl.item(i).getAttributes().getNamedItem("id").getNodeValue();
					EAdBundleId id = new EAdBundleId(bundleId);
					resources.addBundle(id);
					if (bundleId.equals(initialBundleId)) {
						resources.removeBundle(resources.getInitialBundle());
						resources.setInitialBundle(id);
					}
					
					NodeList nl2 = nl.item(i).getChildNodes();
					for (int j = 0, cnt2=nl.getLength(); j<cnt2;j++) {
						AssetDescriptor asset = (AssetDescriptor) VisitorFactory.getVisitor("asset").visit(nl2.item(j));
						resources.addAsset(id, nl2.item(j).getAttributes().getNamedItem("id").getNodeValue(), asset);
					}
				} else {
					AssetDescriptor asset = (AssetDescriptor) VisitorFactory.getVisitor("asset").visit(nl.item(i));
					resources.addAsset(nl.item(i).getAttributes().getNamedItem("id").getNodeValue(), asset);
				}
			}
		
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			field.setAccessible(accessible);
		}
	}

	private void parseMap(Object element, Node newNode, Field field) {
		boolean accessible = field.isAccessible();
		try {
			field.setAccessible(true);
			EAdMap<Object, Object> map = (EAdMap<Object, Object>) field.get(element);
			NodeList nl = newNode.getChildNodes();
			
			for(int i=0, cnt=nl.getLength(); i<cnt; i+=2)
			{
				String type = nl.item(i).getNodeName();
				Object key = VisitorFactory.getVisitor(type).visit(nl.item(i));
				type = nl.item(i+1).getNodeName();
				Object value = VisitorFactory.getVisitor(type).visit(nl.item(i+1));
				map.put(key, value);
			}
		
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			field.setAccessible(accessible);
		}
	}
	
	private void parseList(Object element, Node newNode, Field field) {
		boolean accessible = field.isAccessible();
		try {
			field.setAccessible(true);
			EAdList<Object> list = (EAdList<Object>) field.get(element);
			NodeList nl = newNode.getChildNodes();
			
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				String type = nl.item(i).getNodeName();
				Object object = VisitorFactory.getVisitor(type).visit(nl.item(i));
				list.add(object);
			}
		
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			field.setAccessible(accessible);
		}
	}
	
	
	private void parseParam(Object element, Node newNode, Field field) {
		Object object = VisitorFactory.getVisitor(newNode.getNodeName()).visit(newNode);
		boolean accessible = field.isAccessible();

			try {
				field.setAccessible(true);
				field.set(element, object);
			} catch (IllegalArgumentException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} finally {
				field.setAccessible(accessible);
			}
	}
	

	/**
	 * Gets the {@link Field} object identified by the given id. It gives
	 * precedence to Fields annotated with the id though the {@link Param}
	 * annotation, if non is found it checks if the id coincides with the name
	 * of a field.
	 * 
	 * @param object
	 *            The object for where the field should be
	 * @param paramValue
	 *            The id of the field
	 * @return The field corresponding to the given id
	 */
	public Field getField(Object object, String paramValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				Param a = f.getAnnotation(Param.class);
				if (a != null && a.value().equals(paramValue))
					return f;
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}
}
