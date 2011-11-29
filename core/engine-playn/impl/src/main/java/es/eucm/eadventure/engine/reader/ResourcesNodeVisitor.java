package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * <p>
 * Vistor for the resource element.
 * </p>
 * <p>
 * The resource element should be<br>
 * {@code <resources />} if there are no resources<br>
 * if there are resources then it should be:<br>
 * {@code <resources>}<br>
 * &nbsp;&nbsp;&nbsp;
 * {@code <asset id="ASSET_ID" class="ASSETDESCRIPTOR_CLASS">ASSET_VALUE</asset>}
 * x N<br>
 * {@code </resources>}<br>
 * and if there are bundles:<br>
 * {@code <resources> initialBundle="INITIAL_BUNDLEID"}<br>
 * &nbsp;&nbsp;&nbsp;
 * {@code   <asset id="ASSET_ID" class="ASSETDESCRIPTOR_CLASS">ASSET_VALUE</asset>}
 * x N<br>
 * &nbsp;&nbsp;&nbsp;{@code	<bundle id="BUNDLE_ID">}<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * {@code   <asset id="ASSET_ID" class="ASSETDESCRIPTOR_CLASS">ASSET_VALUE</asset>}
 * x N<br>
 * &nbsp;&nbsp;&nbsp;{@code	</bundle>}<br>
 * {@code </resources>}<br>
 * 
 * </p>
 */
public class ResourcesNodeVisitor extends NodeVisitor<EAdResources> {
	
	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public EAdResources visit(Node node, Field field, Object parent, Class<?> listClass) {
		EAdResources resources = null;
		try {
			resources = (EAdResources) field.getFieldValue(parent);
			
			String initialBundleId = node.getAttributes().getNamedItem(DOMTags.INITIAL_BUNDLE_TAG).getNodeValue();

			NodeList nl = node.getChildNodes();
			
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				if (nl.item(i).getNodeName().equals(DOMTags.BUNDLE_TAG)) {
					String bundleId = nl.item(i).getAttributes().getNamedItem(DOMTags.ID_AT).getNodeValue();
					EAdBundleId id = new EAdBundleId(bundleId);
					resources.addBundle(id);
					if (bundleId.equals(initialBundleId)) {
						resources.setInitialBundle(id);
						resources.removeBundle(resources.getInitialBundle());
					}
					
					NodeList nl2 = nl.item(i).getChildNodes();
					for (int j = 0, cnt2=nl2.getLength(); j<cnt2;j++) {
						AssetDescriptor asset = (AssetDescriptor) VisitorFactory.getVisitor(DOMTags.ASSET_AT).visit(nl2.item(j), null, null, null);
						resources.addAsset(id, nl2.item(j).getAttributes().getNamedItem(DOMTags.ID_AT).getNodeValue(), asset);
					}
				} else {
					AssetDescriptor asset = (AssetDescriptor) VisitorFactory.getVisitor(DOMTags.ASSET_AT).visit(nl.item(i), null, null, null);
					resources.addAsset(nl.item(i).getAttributes().getNamedItem(DOMTags.ID_AT).getNodeValue(), asset);
				}
			}
		
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {

		}
		
		return resources;
	}

	@Override
	public String getNodeType() {
		return DOMTags.RESOURCES_TAG;
	}

}
