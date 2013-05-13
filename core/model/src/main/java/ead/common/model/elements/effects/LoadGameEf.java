package ead.common.model.elements.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.scenes.EAdScene;

@Element
public class LoadGameEf extends AbstractEffect {

	@Param
	private EAdScene loadingScene;

	@Param
	private boolean reloadAssets;

	public LoadGameEf() {

	}

	public LoadGameEf(boolean reloadAssets) {
		this.reloadAssets = reloadAssets;
	}

	public EAdScene getLoadingScene() {
		return loadingScene;
	}

	public void setLoadingScene(EAdScene loadingScene) {
		this.loadingScene = loadingScene;
	}

	public boolean isReloadAssets() {
		return reloadAssets;
	}

	public void setReloadAssets(boolean reloadAssets) {
		this.reloadAssets = reloadAssets;
	}

}
