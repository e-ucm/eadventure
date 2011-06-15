package es.eucm.eadventure.engine.core.platform.assets.impl;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public interface SpecialAssetRenderer<A extends AssetDescriptor, Component> {

	Component getComponent(A asset);
	
}
