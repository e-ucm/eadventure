package es.eucm.eadventure.common.impl.writer;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public class AssetDOMWriter extends FieldParamWriter<AssetDescriptor> {

	@Override
	public Element buildNode(AssetDescriptor assetDescriptor, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.ASSET_AT);

		// Check if asset is new
		int index = mappedAsset.indexOf(assetDescriptor);
		if (index != -1) {
			node.setTextContent("" + index);
			return node;
		}
		
		// Set unique id and class (it has to be in this order)
		node.setAttribute(DOMTags.UNIQUE_ID_AT, mappedAsset.size() + "");
		mappedAsset.add(assetDescriptor);
		node.setAttribute(DOMTags.CLASS_AT, shortClass(assetDescriptor.getClass().getName()));

		// Process Param fields
		super.processParams(node, assetDescriptor);

		return node;
	}

}
