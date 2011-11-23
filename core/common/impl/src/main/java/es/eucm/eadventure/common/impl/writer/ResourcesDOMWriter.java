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

package es.eucm.eadventure.common.impl.writer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.resources.EAdAssetBundle;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.impl.EAdAssetBundleImpl;
import es.eucm.eadventure.common.resources.impl.EAdResourcesImpl;

/**
 * <p>
 * DOM writer for the "resources" element in the eAdventure 2 xml
 * </p>
 * 
 */
public class ResourcesDOMWriter extends DOMWriter<EAdResources> {
	
	public static final String TAG_RESOURCES = "resources";
	
	public static final String TAG_BUNDLE = "bundle";
	
	public static final String INITIAL_BUNDLE_AT = "initialBundle";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.impl.writer.DOMWriter#buildNode(java.lang.Object
	 * )
	 */
	@Override
	public Element buildNode(EAdResources resources) {
		Element node = doc.createElement(TAG_RESOURCES);
		if (resources.getInitialBundle() != null)
			node.setAttribute(INITIAL_BUNDLE_AT, resources.getInitialBundle()
					.getBundleId());

		for (String assetId : ((EAdAssetBundleImpl) resources).getIds()) {
			Node assetNode = processAsset(assetId, resources.getAsset(assetId));
			doc.adoptNode(assetNode);
			node.appendChild(assetNode);
		}

		for (EAdBundleId bundleId : ((EAdResourcesImpl) resources)
				.getBundleIds()) {
			Node bundleNode = processBundle(bundleId,
					((EAdResourcesImpl) resources).getBundle(bundleId));
			node.appendChild(bundleNode);
		}

		return node;
	}

	/**
	 * Create a node from an {@link EAdAssetBundle}
	 * 
	 * @param id
	 *            The {@link EAdBundleId} of the bundle
	 * @param bundle
	 *            The {@link EAdAssetBundle} with the information
	 * @return the node created with the bundle information
	 */
	private Node processBundle(EAdBundleId id, EAdAssetBundle bundle) {
		Element bundleNode = doc.createElement(TAG_BUNDLE);
		bundleNode.setAttribute(DOMTags.ID_AT, id.getBundleId());

		for (String assetId : ((EAdAssetBundleImpl) bundle).getIds()) {
			Node assetNode = processAsset(assetId, bundle.getAsset(assetId));
			doc.adoptNode(assetNode);
			bundleNode.appendChild(assetNode);
		}

		return bundleNode;
	}

	/**
	 * Create a node from an {@link AssetDescriptor}
	 * 
	 * @param id
	 *            The id of the asset
	 * @param assetDescriptor
	 *            The {@link AssetDescriptor}
	 * @return the node created with the asset information
	 */
	private Node processAsset(String id, AssetDescriptor assetDescriptor) {
		Element assetNode = super.initNode(assetDescriptor);
		assetNode.setAttribute(DOMTags.ID_AT, id);
		return assetNode;
	}
}
