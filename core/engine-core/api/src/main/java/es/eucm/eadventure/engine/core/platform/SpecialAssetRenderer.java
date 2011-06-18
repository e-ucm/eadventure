package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public interface SpecialAssetRenderer<A extends AssetDescriptor, Component> {

	Component getComponent(A asset);

	boolean isFinished();
	
}
