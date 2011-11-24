package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

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
	
	public static final String TAG = "element";

	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public EAdResources visit(Node node, Field field, Object parent) {
		EAdResources resources = null;
		try {
			resources = (EAdResources) field.getFieldValue(parent);

			if (node == null || node.getAttributes() == null)
				logger.severe("Unexpected null");

			
			String initialBundleId = node.getAttributes().getNamedItem("initialBundle").getNodeValue();

			NodeList nl = node.getChildNodes();
			
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				if (nl.item(i).getNodeName().equals("bundle")) {
					if ( nl.item(i) == null ||  nl.item(i).getAttributes() == null)
						logger.severe("Unexpected null in  nl.item(i)");

					String bundleId = nl.item(i).getAttributes().getNamedItem("id").getNodeValue();
					EAdBundleId id = new EAdBundleId(bundleId);
					resources.addBundle(id);
					if (bundleId.equals(initialBundleId)) {
						resources.removeBundle(resources.getInitialBundle());
						resources.setInitialBundle(id);
					}
					
					NodeList nl2 = nl.item(i).getChildNodes();
					for (int j = 0, cnt2=nl2.getLength(); j<cnt2;j++) {
						if ( nl2.item(j) == null ||  nl2.item(j).getAttributes() == null)
							logger.severe("Unexpected null in  nl2.item(j)");

						AssetDescriptor asset = (AssetDescriptor) VisitorFactory.getVisitor("asset").visit(nl2.item(j), null, null);
						resources.addAsset(id, nl2.item(j).getAttributes().getNamedItem("id").getNodeValue(), asset);
					}
				} else {
					if ( nl.item(i) == null ||  nl.item(i).getAttributes() == null)
						logger.severe("Unexpected null in  nl.item(i)");

					
					AssetDescriptor asset = (AssetDescriptor) VisitorFactory.getVisitor("asset").visit(nl.item(i), null, null);
					resources.addAsset(nl.item(i).getAttributes().getNamedItem("id").getNodeValue(), asset);
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
		return "resources";
	}

}
