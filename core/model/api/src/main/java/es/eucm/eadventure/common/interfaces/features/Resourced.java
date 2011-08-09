package es.eucm.eadventure.common.interfaces.features;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * Represents an {@link EAdElement} with resources associated
 * 
 */
public interface Resourced {
	/**
	 * Get the asset for the given id.
	 * 
	 * @param id
	 *            The id of the asset
	 * @return The asset with that id
	 */
	AssetDescriptor getAsset(String id);

	/**
	 * Get the asset for the given id.
	 * 
	 * @param id
	 *            The id of the asset
	 * @return The asset with that id
	 */
	AssetDescriptor getAsset(EAdBundleId bundleId, String id);

	/**
	 * Get the bundle {@link EAdBunldeId} initially selected for the element.
	 * The initial or default bundle of the element is a parameter set during
	 * edition.
	 * 
	 * 
	 * @return The initial bundle of assets. Initial bundle could be null for
	 *         elements with no initial bundle.
	 */
	EAdBundleId getInitialBundle();

	/**
	 * Get the resources {@link EAdResources} of this element.
	 * 
	 * @return The resources associated with the element. Resources should never
	 *         be null.
	 */
	EAdResources getResources();
}
