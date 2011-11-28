package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.impl.reader.extra.ObjectFactory;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * <p>
 * Visitor for the asset element of resources.
 * </p>
 * <p>
 * The asset element should be<br>
 * {@code <asset id="ASSET_ID" class="ASSETDESCRIPTOR_CLASS">ASSET_VALUE</asset>}
 * <br>
 * </p>
 */
public class AssetNodeVisitor extends NodeVisitor<AssetDescriptor> {
	
	public static final String TAG = "element";

	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public AssetDescriptor visit(Node node, Field field, Object parent, Class<?> listClass) {
		AssetDescriptor element =  (AssetDescriptor) ObjectFactory.getObject(node.getTextContent(), AssetDescriptor.class);
		if (element != null) {
			setValue(field, parent, element);
			return element;
		}
			
		String uniqueId = node.getAttributes().getNamedItem(DOMTags.UNIQUE_ID_AT).getNodeValue();

		String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
		clazz = translateClass(clazz);
		
		Class<?> c = null;
		try {
			c = ClassLoader.getSystemClassLoader().loadClass(clazz);
			Constructor<?> con = c.getConstructor();
			element = (AssetDescriptor) con.newInstance(new Object[] { });
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
		if (element != null)
			ObjectFactory.addAsset(uniqueId, element);
		setValue(field, parent, element);

		
		readFields(element, node);
		
		return element;
	}
	
	


	@Override
	public String getNodeType() {
		return "asset";
	}

}
