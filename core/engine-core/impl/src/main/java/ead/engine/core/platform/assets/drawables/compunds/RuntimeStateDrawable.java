package ead.engine.core.platform.assets.drawables.compunds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import ead.common.resources.assets.drawable.compounds.EAdStateDrawable;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class RuntimeStateDrawable extends
		AbstractRuntimeAsset<EAdStateDrawable> implements
		RuntimeCompoundDrawable<EAdStateDrawable> {

	private AssetHandler assetHandler;

	private Map<String, RuntimeCompoundDrawable<?>> drawables;

	private boolean loaded;

	private boolean loading;

	@Inject
	public RuntimeStateDrawable(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		drawables = new HashMap<String, RuntimeCompoundDrawable<?>>();
		loaded = false;
		loading = false;
	}

	@Override
	public boolean loadAsset() {
		loading = true;
		for (String s : descriptor.getStates()) {
			RuntimeCompoundDrawable<?> r = (RuntimeCompoundDrawable<?>) assetHandler
					.getRuntimeAsset(descriptor.getDrawable(s), true);
			drawables.put(s, r);
		}
		return true;
	}

	@Override
	public void freeMemory() {
		for (RuntimeCompoundDrawable<?> r : drawables.values()) {
			r.freeMemory();
		}
		drawables.clear();
	}

	@Override
	public boolean isLoaded() {
		if (loading) {
			loaded = true;
			for (RuntimeCompoundDrawable<?> r : drawables.values()) {
				if (!r.loadAsset()) {
					loaded = false;
					break;
				}
				loading = false;
			}
		}
		return loaded;
	}

	@Override
	public void update() {

	}

	@Override
	public RuntimeDrawable<?, ?> getDrawable(int time, List<String> states,
			int level) {
		String state = states.get(level);
		RuntimeCompoundDrawable<?> d = drawables.get(state);
		return d.getDrawable(time, states, ++level);
	}

}
