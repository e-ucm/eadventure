package ead.engine.core.platform.assets.text;

import ead.common.resources.assets.text.EAdFont;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.RuntimeFont;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;

public abstract class BasicRuntimeFont extends AbstractRuntimeAsset<EAdFont>
		implements RuntimeFont {

	private boolean loaded;

	protected AssetHandler assetHandler;

	public BasicRuntimeFont(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
	}

	@Override
	public boolean loadAsset() {
		loaded = true;
		return loaded;
	}

	@Override
	public void freeMemory() {
		loaded = false;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public void update() {

	}

	public EAdFont getEAdFont() {
		return descriptor;
	}

}
