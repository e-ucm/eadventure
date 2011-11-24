package es.eucm.eadventure.engine.reader;

import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

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
	public AssetDescriptor visit(Node node, Field field, Object parent) {
		AssetDescriptor element =  (AssetDescriptor) ObjectFactory.getObject(GWTReader.getNodeText(node), AssetDescriptor.class);
		if (element != null) {
			setValue(field, parent, element);
			return element;
		}
			
		if (node == null || node.getAttributes() == null)
			logger.severe("Unexpected null");
		
		String uniqueId = node.getAttributes().getNamedItem(DOMTags.UNIQUE_ID_AT).getNodeValue();
		String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
		clazz = translateClass(clazz);
		
		ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);
		element = (AssetDescriptor) classType.findConstructor().newInstance();
		
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
