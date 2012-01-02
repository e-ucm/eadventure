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

package ead.common.reader.visitors;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ead.common.DOMTags;
import ead.common.resources.EAdBundleId;
import ead.common.resources.EAdResources;
import ead.common.resources.assets.AssetDescriptor;

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
		boolean accessible = field.isAccessible();
		EAdResources resources = null;
		try {
			field.setAccessible(true);
			resources = (EAdResources) field.get(parent);
			
			String initialBundleId = node.getAttributes().getNamedItem(DOMTags.INITIAL_BUNDLE_TAG).getNodeValue();

			NodeList nl = node.getChildNodes();
			
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				if (nl.item(i).getNodeName().equals(DOMTags.BUNDLE_TAG)) {
					String bundleId = nl.item(i).getAttributes().getNamedItem(DOMTags.ID_AT).getNodeValue();
					EAdBundleId id = new EAdBundleId(bundleId);
					resources.addBundle(id);
					if (bundleId.equals(initialBundleId) && !id.equals(resources.getInitialBundle())) {
						EAdBundleId temp = resources.getInitialBundle();
						resources.setInitialBundle(id);
						resources.removeBundle(temp);
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
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			field.setAccessible(accessible);
		}
		
		return resources;
	}
	
	


	@Override
	public String getNodeType() {
		return DOMTags.RESOURCES_TAG;
	}

}
